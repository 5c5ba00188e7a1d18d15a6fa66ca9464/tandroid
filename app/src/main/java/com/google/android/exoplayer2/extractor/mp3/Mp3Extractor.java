package com.google.android.exoplayer2.extractor.mp3;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.GaplessInfoHolder;
import com.google.android.exoplayer2.extractor.Id3Peeker;
import com.google.android.exoplayer2.extractor.MpegAudioHeader;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.extractor.mp3.Seeker;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.id3.Id3Decoder;
import com.google.android.exoplayer2.metadata.id3.MlltFrame;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.io.EOFException;
import java.io.IOException;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/* loaded from: classes3.dex */
public final class Mp3Extractor implements Extractor {
    public static final int FLAG_DISABLE_ID3_METADATA = 2;
    public static final int FLAG_ENABLE_CONSTANT_BITRATE_SEEKING = 1;
    private static final int MAX_SNIFF_BYTES = 16384;
    private static final int MAX_SYNC_BYTES = 131072;
    private static final int MPEG_AUDIO_HEADER_MASK = -128000;
    private static final int SCRATCH_LENGTH = 10;
    private static final int SEEK_HEADER_INFO = 1231971951;
    private static final int SEEK_HEADER_UNSET = 0;
    private static final int SEEK_HEADER_VBRI = 1447187017;
    private static final int SEEK_HEADER_XING = 1483304551;
    private long basisTimeUs;
    private boolean disableSeeking;
    private ExtractorOutput extractorOutput;
    private long firstSamplePosition;
    private final int flags;
    private final long forcedFirstSampleTimestampUs;
    private final GaplessInfoHolder gaplessInfoHolder;
    private final Id3Peeker id3Peeker;
    private Metadata metadata;
    private int sampleBytesRemaining;
    private long samplesRead;
    private final ParsableByteArray scratch;
    private Seeker seeker;
    private final MpegAudioHeader synchronizedHeader;
    private int synchronizedHeaderData;
    private TrackOutput trackOutput;
    public static final ExtractorsFactory FACTORY = Mp3Extractor$$ExternalSyntheticLambda0.INSTANCE;
    private static final Id3Decoder.FramePredicate REQUIRED_ID3_FRAME_PREDICATE = Mp3Extractor$$ExternalSyntheticLambda1.INSTANCE;

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface Flags {
    }

    public static /* synthetic */ Extractor[] lambda$static$0() {
        return new Extractor[]{new Mp3Extractor()};
    }

    public static /* synthetic */ boolean lambda$static$1(int majorVersion, int id0, int id1, int id2, int id3) {
        return (id0 == 67 && id1 == 79 && id2 == 77 && (id3 == 77 || majorVersion == 2)) || (id0 == 77 && id1 == 76 && id2 == 76 && (id3 == 84 || majorVersion == 2));
    }

    public Mp3Extractor() {
        this(0);
    }

    public Mp3Extractor(int flags) {
        this(flags, C.TIME_UNSET);
    }

    public Mp3Extractor(int flags, long forcedFirstSampleTimestampUs) {
        this.flags = flags;
        this.forcedFirstSampleTimestampUs = forcedFirstSampleTimestampUs;
        this.scratch = new ParsableByteArray(10);
        this.synchronizedHeader = new MpegAudioHeader();
        this.gaplessInfoHolder = new GaplessInfoHolder();
        this.basisTimeUs = C.TIME_UNSET;
        this.id3Peeker = new Id3Peeker();
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public boolean sniff(ExtractorInput input) throws IOException, InterruptedException {
        return synchronize(input, true);
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public void init(ExtractorOutput output) {
        this.extractorOutput = output;
        this.trackOutput = output.track(0, 1);
        this.extractorOutput.endTracks();
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public void seek(long position, long timeUs) {
        this.synchronizedHeaderData = 0;
        this.basisTimeUs = C.TIME_UNSET;
        this.samplesRead = 0L;
        this.sampleBytesRemaining = 0;
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public void release() {
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public int read(ExtractorInput input, PositionHolder seekPosition) throws IOException, InterruptedException {
        if (this.synchronizedHeaderData == 0) {
            try {
                synchronize(input, false);
            } catch (EOFException e) {
                return -1;
            }
        }
        if (this.seeker == null) {
            Seeker seekFrameSeeker = maybeReadSeekFrame(input);
            Seeker metadataSeeker = maybeHandleSeekMetadata(this.metadata, input.getPosition());
            if (this.disableSeeking) {
                this.seeker = new Seeker.UnseekableSeeker();
            } else {
                if (metadataSeeker != null) {
                    this.seeker = metadataSeeker;
                } else if (seekFrameSeeker != null) {
                    this.seeker = seekFrameSeeker;
                }
                Seeker seeker = this.seeker;
                if (seeker == null || (!seeker.isSeekable() && (this.flags & 1) != 0)) {
                    this.seeker = getConstantBitrateSeeker(input);
                }
            }
            this.extractorOutput.seekMap(this.seeker);
            this.trackOutput.format(Format.createAudioSampleFormat(null, this.synchronizedHeader.mimeType, null, -1, 4096, this.synchronizedHeader.channels, this.synchronizedHeader.sampleRate, -1, this.gaplessInfoHolder.encoderDelay, this.gaplessInfoHolder.encoderPadding, null, null, 0, null, (this.flags & 2) != 0 ? null : this.metadata));
            this.firstSamplePosition = input.getPosition();
        } else if (this.firstSamplePosition != 0) {
            long inputPosition = input.getPosition();
            long j = this.firstSamplePosition;
            if (inputPosition < j) {
                input.skipFully((int) (j - inputPosition));
            }
        }
        return readSample(input);
    }

    public void disableSeeking() {
        this.disableSeeking = true;
    }

    private int readSample(ExtractorInput extractorInput) throws IOException, InterruptedException {
        if (this.sampleBytesRemaining == 0) {
            extractorInput.resetPeekPosition();
            if (peekEndOfStreamOrHeader(extractorInput)) {
                return -1;
            }
            this.scratch.setPosition(0);
            int sampleHeaderData = this.scratch.readInt();
            if (!headersMatch(sampleHeaderData, this.synchronizedHeaderData) || MpegAudioHeader.getFrameSize(sampleHeaderData) == -1) {
                extractorInput.skipFully(1);
                this.synchronizedHeaderData = 0;
                return 0;
            }
            MpegAudioHeader.populateHeader(sampleHeaderData, this.synchronizedHeader);
            if (this.basisTimeUs == C.TIME_UNSET) {
                this.basisTimeUs = this.seeker.getTimeUs(extractorInput.getPosition());
                if (this.forcedFirstSampleTimestampUs != C.TIME_UNSET) {
                    long embeddedFirstSampleTimestampUs = this.seeker.getTimeUs(0L);
                    this.basisTimeUs += this.forcedFirstSampleTimestampUs - embeddedFirstSampleTimestampUs;
                }
            }
            this.sampleBytesRemaining = this.synchronizedHeader.frameSize;
        }
        int bytesAppended = this.trackOutput.sampleData(extractorInput, this.sampleBytesRemaining, true);
        if (bytesAppended == -1) {
            return -1;
        }
        int i = this.sampleBytesRemaining - bytesAppended;
        this.sampleBytesRemaining = i;
        if (i > 0) {
            return 0;
        }
        long timeUs = this.basisTimeUs + ((this.samplesRead * 1000000) / this.synchronizedHeader.sampleRate);
        this.trackOutput.sampleMetadata(timeUs, 1, this.synchronizedHeader.frameSize, 0, null);
        this.samplesRead += this.synchronizedHeader.samplesPerFrame;
        this.sampleBytesRemaining = 0;
        return 0;
    }

    /* JADX WARN: Code restructure failed: missing block: B:48:0x009d, code lost:
        if (r14 == false) goto L50;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x009f, code lost:
        r13.skipFully(r2 + r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x00a5, code lost:
        r13.resetPeekPosition();
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x00a8, code lost:
        r12.synchronizedHeaderData = r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x00aa, code lost:
        return true;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean synchronize(ExtractorInput input, boolean sniffing) throws IOException, InterruptedException {
        int frameSize;
        int validFrameCount = 0;
        int candidateSynchronizedHeaderData = 0;
        int peekedId3Bytes = 0;
        int searchedBytes = 0;
        int searchLimitBytes = sniffing ? 16384 : 131072;
        input.resetPeekPosition();
        if (input.getPosition() == 0) {
            boolean parseAllId3Frames = (this.flags & 2) == 0;
            Id3Decoder.FramePredicate id3FramePredicate = parseAllId3Frames ? null : REQUIRED_ID3_FRAME_PREDICATE;
            Metadata peekId3Data = this.id3Peeker.peekId3Data(input, id3FramePredicate);
            this.metadata = peekId3Data;
            if (peekId3Data != null) {
                this.gaplessInfoHolder.setFromMetadata(peekId3Data);
            }
            peekedId3Bytes = (int) input.getPeekPosition();
            if (!sniffing) {
                input.skipFully(peekedId3Bytes);
            }
        }
        while (true) {
            boolean parseAllId3Frames2 = peekEndOfStreamOrHeader(input);
            if (parseAllId3Frames2) {
                if (validFrameCount <= 0) {
                    throw new EOFException();
                }
            } else {
                this.scratch.setPosition(0);
                int headerData = this.scratch.readInt();
                if ((candidateSynchronizedHeaderData != 0 && !headersMatch(headerData, candidateSynchronizedHeaderData)) || (frameSize = MpegAudioHeader.getFrameSize(headerData)) == -1) {
                    int searchedBytes2 = searchedBytes + 1;
                    if (searchedBytes == searchLimitBytes) {
                        if (!sniffing) {
                            throw new ParserException("Searched too many bytes.");
                        }
                        return false;
                    }
                    validFrameCount = 0;
                    candidateSynchronizedHeaderData = 0;
                    if (sniffing) {
                        input.resetPeekPosition();
                        input.advancePeekPosition(peekedId3Bytes + searchedBytes2);
                    } else {
                        input.skipFully(1);
                    }
                    searchedBytes = searchedBytes2;
                } else {
                    validFrameCount++;
                    if (validFrameCount == 1) {
                        MpegAudioHeader.populateHeader(headerData, this.synchronizedHeader);
                        candidateSynchronizedHeaderData = headerData;
                    } else if (validFrameCount == 4) {
                        break;
                    }
                    input.advancePeekPosition(frameSize - 4);
                }
            }
        }
    }

    private boolean peekEndOfStreamOrHeader(ExtractorInput extractorInput) throws IOException, InterruptedException {
        Seeker seeker = this.seeker;
        if (seeker != null) {
            long dataEndPosition = seeker.getDataEndPosition();
            if (dataEndPosition != -1 && extractorInput.getPeekPosition() > dataEndPosition - 4) {
                return true;
            }
        }
        try {
            return !extractorInput.peekFully(this.scratch.data, 0, 4, true);
        } catch (EOFException e) {
            return true;
        }
    }

    private Seeker maybeReadSeekFrame(ExtractorInput input) throws IOException, InterruptedException {
        ParsableByteArray frame = new ParsableByteArray(this.synchronizedHeader.frameSize);
        input.peekFully(frame.data, 0, this.synchronizedHeader.frameSize);
        int i = 21;
        if ((this.synchronizedHeader.version & 1) != 0) {
            if (this.synchronizedHeader.channels != 1) {
                i = 36;
            }
        } else if (this.synchronizedHeader.channels == 1) {
            i = 13;
        }
        int xingBase = i;
        int seekHeader = getSeekFrameHeader(frame, xingBase);
        if (seekHeader == SEEK_HEADER_XING || seekHeader == SEEK_HEADER_INFO) {
            Seeker seeker = XingSeeker.create(input.getLength(), input.getPosition(), this.synchronizedHeader, frame);
            if (seeker != null && !this.gaplessInfoHolder.hasGaplessInfo()) {
                input.resetPeekPosition();
                input.advancePeekPosition(xingBase + 141);
                input.peekFully(this.scratch.data, 0, 3);
                this.scratch.setPosition(0);
                this.gaplessInfoHolder.setFromXingHeaderValue(this.scratch.readUnsignedInt24());
            }
            input.skipFully(this.synchronizedHeader.frameSize);
            if (seeker != null && !seeker.isSeekable() && seekHeader == SEEK_HEADER_INFO) {
                return getConstantBitrateSeeker(input);
            }
            return seeker;
        } else if (seekHeader == SEEK_HEADER_VBRI) {
            Seeker seeker2 = VbriSeeker.create(input.getLength(), input.getPosition(), this.synchronizedHeader, frame);
            input.skipFully(this.synchronizedHeader.frameSize);
            return seeker2;
        } else {
            input.resetPeekPosition();
            return null;
        }
    }

    private Seeker getConstantBitrateSeeker(ExtractorInput input) throws IOException, InterruptedException {
        input.peekFully(this.scratch.data, 0, 4);
        this.scratch.setPosition(0);
        MpegAudioHeader.populateHeader(this.scratch.readInt(), this.synchronizedHeader);
        return new ConstantBitrateSeeker(input.getLength(), input.getPosition(), this.synchronizedHeader);
    }

    private static boolean headersMatch(int headerA, long headerB) {
        return ((long) (MPEG_AUDIO_HEADER_MASK & headerA)) == ((-128000) & headerB);
    }

    private static int getSeekFrameHeader(ParsableByteArray frame, int xingBase) {
        if (frame.limit() >= xingBase + 4) {
            frame.setPosition(xingBase);
            int headerData = frame.readInt();
            if (headerData == SEEK_HEADER_XING || headerData == SEEK_HEADER_INFO) {
                return headerData;
            }
        }
        if (frame.limit() >= 40) {
            frame.setPosition(36);
            if (frame.readInt() != SEEK_HEADER_VBRI) {
                return 0;
            }
            return SEEK_HEADER_VBRI;
        }
        return 0;
    }

    private static MlltSeeker maybeHandleSeekMetadata(Metadata metadata, long firstFramePosition) {
        if (metadata != null) {
            int length = metadata.length();
            for (int i = 0; i < length; i++) {
                Metadata.Entry entry = metadata.get(i);
                if (entry instanceof MlltFrame) {
                    return MlltSeeker.create(firstFramePosition, (MlltFrame) entry);
                }
            }
            return null;
        }
        return null;
    }
}
