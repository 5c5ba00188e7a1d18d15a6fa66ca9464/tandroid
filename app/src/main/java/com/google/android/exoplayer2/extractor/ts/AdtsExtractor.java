package com.google.android.exoplayer2.extractor.ts;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.extractor.ConstantBitrateSeekMap;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.ts.TsPayloadReader;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.ParsableBitArray;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.io.EOFException;
import java.io.IOException;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/* loaded from: classes3.dex */
public final class AdtsExtractor implements Extractor {
    public static final ExtractorsFactory FACTORY = AdtsExtractor$$ExternalSyntheticLambda0.INSTANCE;
    public static final int FLAG_ENABLE_CONSTANT_BITRATE_SEEKING = 1;
    private static final int MAX_PACKET_SIZE = 2048;
    private static final int MAX_SNIFF_BYTES = 8192;
    private static final int NUM_FRAMES_FOR_AVERAGE_FRAME_SIZE = 1000;
    private int averageFrameSize;
    private ExtractorOutput extractorOutput;
    private long firstFramePosition;
    private long firstSampleTimestampUs;
    private final int flags;
    private boolean hasCalculatedAverageFrameSize;
    private boolean hasOutputSeekMap;
    private final ParsableByteArray packetBuffer;
    private final AdtsReader reader;
    private final ParsableByteArray scratch;
    private final ParsableBitArray scratchBits;
    private boolean startedPacket;

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface Flags {
    }

    public static /* synthetic */ Extractor[] lambda$static$0() {
        return new Extractor[]{new AdtsExtractor()};
    }

    public AdtsExtractor() {
        this(0);
    }

    public AdtsExtractor(int flags) {
        this.flags = flags;
        this.reader = new AdtsReader(true);
        this.packetBuffer = new ParsableByteArray(2048);
        this.averageFrameSize = -1;
        this.firstFramePosition = -1L;
        ParsableByteArray parsableByteArray = new ParsableByteArray(10);
        this.scratch = parsableByteArray;
        this.scratchBits = new ParsableBitArray(parsableByteArray.data);
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public boolean sniff(ExtractorInput input) throws IOException, InterruptedException {
        int startPosition = peekId3Header(input);
        int headerPosition = startPosition;
        int totalValidFramesSize = 0;
        int validFramesCount = 0;
        while (true) {
            input.peekFully(this.scratch.data, 0, 2);
            this.scratch.setPosition(0);
            int syncBytes = this.scratch.readUnsignedShort();
            if (!AdtsReader.isAdtsSyncWord(syncBytes)) {
                validFramesCount = 0;
                totalValidFramesSize = 0;
                input.resetPeekPosition();
                headerPosition++;
                if (headerPosition - startPosition >= 8192) {
                    return false;
                }
                input.advancePeekPosition(headerPosition);
            } else {
                validFramesCount++;
                if (validFramesCount >= 4 && totalValidFramesSize > 188) {
                    return true;
                }
                input.peekFully(this.scratch.data, 0, 4);
                this.scratchBits.setPosition(14);
                int frameSize = this.scratchBits.readBits(13);
                if (frameSize <= 6) {
                    return false;
                }
                input.advancePeekPosition(frameSize - 6);
                totalValidFramesSize += frameSize;
            }
        }
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public void init(ExtractorOutput output) {
        this.extractorOutput = output;
        this.reader.createTracks(output, new TsPayloadReader.TrackIdGenerator(0, 1));
        output.endTracks();
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public void seek(long position, long timeUs) {
        this.startedPacket = false;
        this.reader.seek();
        this.firstSampleTimestampUs = timeUs;
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public void release() {
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public int read(ExtractorInput input, PositionHolder seekPosition) throws IOException, InterruptedException {
        long inputLength = input.getLength();
        boolean canUseConstantBitrateSeeking = ((this.flags & 1) == 0 || inputLength == -1) ? false : true;
        if (canUseConstantBitrateSeeking) {
            calculateAverageFrameSize(input);
        }
        int bytesRead = input.read(this.packetBuffer.data, 0, 2048);
        boolean readEndOfStream = bytesRead == -1;
        maybeOutputSeekMap(inputLength, canUseConstantBitrateSeeking, readEndOfStream);
        if (readEndOfStream) {
            return -1;
        }
        this.packetBuffer.setPosition(0);
        this.packetBuffer.setLimit(bytesRead);
        if (!this.startedPacket) {
            this.reader.packetStarted(this.firstSampleTimestampUs, 4);
            this.startedPacket = true;
        }
        this.reader.consume(this.packetBuffer);
        return 0;
    }

    private int peekId3Header(ExtractorInput input) throws IOException, InterruptedException {
        int firstFramePosition = 0;
        while (true) {
            input.peekFully(this.scratch.data, 0, 10);
            this.scratch.setPosition(0);
            if (this.scratch.readUnsignedInt24() != 4801587) {
                break;
            }
            this.scratch.skipBytes(3);
            int length = this.scratch.readSynchSafeInt();
            firstFramePosition += length + 10;
            input.advancePeekPosition(length);
        }
        input.resetPeekPosition();
        input.advancePeekPosition(firstFramePosition);
        if (this.firstFramePosition == -1) {
            this.firstFramePosition = firstFramePosition;
        }
        return firstFramePosition;
    }

    private void maybeOutputSeekMap(long inputLength, boolean canUseConstantBitrateSeeking, boolean readEndOfStream) {
        if (this.hasOutputSeekMap) {
            return;
        }
        boolean useConstantBitrateSeeking = canUseConstantBitrateSeeking && this.averageFrameSize > 0;
        if (useConstantBitrateSeeking && this.reader.getSampleDurationUs() == C.TIME_UNSET && !readEndOfStream) {
            return;
        }
        ExtractorOutput extractorOutput = (ExtractorOutput) Assertions.checkNotNull(this.extractorOutput);
        if (useConstantBitrateSeeking && this.reader.getSampleDurationUs() != C.TIME_UNSET) {
            extractorOutput.seekMap(getConstantBitrateSeekMap(inputLength));
        } else {
            extractorOutput.seekMap(new SeekMap.Unseekable(C.TIME_UNSET));
        }
        this.hasOutputSeekMap = true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:27:0x006c, code lost:
        r9.hasCalculatedAverageFrameSize = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0075, code lost:
        throw new com.google.android.exoplayer2.ParserException("Malformed ADTS stream");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void calculateAverageFrameSize(ExtractorInput input) throws IOException, InterruptedException {
        if (this.hasCalculatedAverageFrameSize) {
            return;
        }
        this.averageFrameSize = -1;
        input.resetPeekPosition();
        if (input.getPosition() == 0) {
            peekId3Header(input);
        }
        int numValidFrames = 0;
        long totalValidFramesSize = 0;
        while (true) {
            try {
                if (!input.peekFully(this.scratch.data, 0, 2, true)) {
                    break;
                }
                this.scratch.setPosition(0);
                int syncBytes = this.scratch.readUnsignedShort();
                if (AdtsReader.isAdtsSyncWord(syncBytes)) {
                    if (input.peekFully(this.scratch.data, 0, 4, true)) {
                        this.scratchBits.setPosition(14);
                        int currentFrameSize = this.scratchBits.readBits(13);
                        if (currentFrameSize <= 6) {
                            break;
                        }
                        totalValidFramesSize += currentFrameSize;
                        numValidFrames++;
                        if (numValidFrames != 1000 && input.advancePeekPosition(currentFrameSize - 6, true)) {
                        }
                    } else {
                        break;
                    }
                } else {
                    numValidFrames = 0;
                    break;
                }
            } catch (EOFException e) {
            }
        }
        input.resetPeekPosition();
        if (numValidFrames > 0) {
            this.averageFrameSize = (int) (totalValidFramesSize / numValidFrames);
        } else {
            this.averageFrameSize = -1;
        }
        this.hasCalculatedAverageFrameSize = true;
    }

    private SeekMap getConstantBitrateSeekMap(long inputLength) {
        int bitrate = getBitrateFromFrameSize(this.averageFrameSize, this.reader.getSampleDurationUs());
        return new ConstantBitrateSeekMap(inputLength, this.firstFramePosition, bitrate, this.averageFrameSize);
    }

    private static int getBitrateFromFrameSize(int frameSize, long durationUsPerFrame) {
        return (int) (((frameSize * 8) * 1000000) / durationUsPerFrame);
    }
}
