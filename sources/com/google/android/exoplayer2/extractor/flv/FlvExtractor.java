package com.google.android.exoplayer2.extractor.flv;

import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.io.IOException;
/* loaded from: classes.dex */
public final class FlvExtractor implements Extractor {
    private AudioTagPayloadReader audioReader;
    private int bytesToNextTagHeader;
    private ExtractorOutput extractorOutput;
    private long mediaTagTimestampOffsetUs;
    private boolean outputFirstSample;
    private boolean outputSeekMap;
    private int tagDataSize;
    private long tagTimestampUs;
    private int tagType;
    private VideoTagPayloadReader videoReader;
    private final ParsableByteArray scratch = new ParsableByteArray(4);
    private final ParsableByteArray headerBuffer = new ParsableByteArray(9);
    private final ParsableByteArray tagHeaderBuffer = new ParsableByteArray(11);
    private final ParsableByteArray tagData = new ParsableByteArray();
    private final ScriptTagPayloadReader metadataReader = new ScriptTagPayloadReader();
    private int state = 1;

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public void release() {
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public boolean sniff(ExtractorInput extractorInput) throws IOException, InterruptedException {
        extractorInput.peekFully(this.scratch.data, 0, 3);
        this.scratch.setPosition(0);
        if (this.scratch.readUnsignedInt24() != 4607062) {
            return false;
        }
        extractorInput.peekFully(this.scratch.data, 0, 2);
        this.scratch.setPosition(0);
        if ((this.scratch.readUnsignedShort() & 250) != 0) {
            return false;
        }
        extractorInput.peekFully(this.scratch.data, 0, 4);
        this.scratch.setPosition(0);
        int readInt = this.scratch.readInt();
        extractorInput.resetPeekPosition();
        extractorInput.advancePeekPosition(readInt);
        extractorInput.peekFully(this.scratch.data, 0, 4);
        this.scratch.setPosition(0);
        return this.scratch.readInt() == 0;
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public void init(ExtractorOutput extractorOutput) {
        this.extractorOutput = extractorOutput;
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public void seek(long j, long j2) {
        this.state = 1;
        this.outputFirstSample = false;
        this.bytesToNextTagHeader = 0;
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public int read(ExtractorInput extractorInput, PositionHolder positionHolder) throws IOException, InterruptedException {
        while (true) {
            int i = this.state;
            if (i != 1) {
                if (i == 2) {
                    skipToTagHeader(extractorInput);
                } else if (i != 3) {
                    if (i == 4) {
                        if (readTagData(extractorInput)) {
                            return 0;
                        }
                    } else {
                        throw new IllegalStateException();
                    }
                } else if (!readTagHeader(extractorInput)) {
                    return -1;
                }
            } else if (!readFlvHeader(extractorInput)) {
                return -1;
            }
        }
    }

    private boolean readFlvHeader(ExtractorInput extractorInput) throws IOException, InterruptedException {
        if (extractorInput.readFully(this.headerBuffer.data, 0, 9, true)) {
            this.headerBuffer.setPosition(0);
            this.headerBuffer.skipBytes(4);
            int readUnsignedByte = this.headerBuffer.readUnsignedByte();
            boolean z = (readUnsignedByte & 4) != 0;
            boolean z2 = (readUnsignedByte & 1) != 0;
            if (z && this.audioReader == null) {
                this.audioReader = new AudioTagPayloadReader(this.extractorOutput.track(8, 1));
            }
            if (z2 && this.videoReader == null) {
                this.videoReader = new VideoTagPayloadReader(this.extractorOutput.track(9, 2));
            }
            this.extractorOutput.endTracks();
            this.bytesToNextTagHeader = (this.headerBuffer.readInt() - 9) + 4;
            this.state = 2;
            return true;
        }
        return false;
    }

    private void skipToTagHeader(ExtractorInput extractorInput) throws IOException, InterruptedException {
        extractorInput.skipFully(this.bytesToNextTagHeader);
        this.bytesToNextTagHeader = 0;
        this.state = 3;
    }

    private boolean readTagHeader(ExtractorInput extractorInput) throws IOException, InterruptedException {
        if (extractorInput.readFully(this.tagHeaderBuffer.data, 0, 11, true)) {
            this.tagHeaderBuffer.setPosition(0);
            this.tagType = this.tagHeaderBuffer.readUnsignedByte();
            this.tagDataSize = this.tagHeaderBuffer.readUnsignedInt24();
            this.tagTimestampUs = this.tagHeaderBuffer.readUnsignedInt24();
            this.tagTimestampUs = ((this.tagHeaderBuffer.readUnsignedByte() << 24) | this.tagTimestampUs) * 1000;
            this.tagHeaderBuffer.skipBytes(3);
            this.state = 4;
            return true;
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x007b  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x007f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean readTagData(ExtractorInput extractorInput) throws IOException, InterruptedException {
        boolean z;
        long currentTimestampUs = getCurrentTimestampUs();
        int i = this.tagType;
        boolean z2 = false;
        if (i == 8 && this.audioReader != null) {
            ensureReadyForMediaOutput();
            z2 = this.audioReader.consume(prepareTagData(extractorInput), currentTimestampUs);
        } else if (i == 9 && this.videoReader != null) {
            ensureReadyForMediaOutput();
            z2 = this.videoReader.consume(prepareTagData(extractorInput), currentTimestampUs);
        } else if (i == 18 && !this.outputSeekMap) {
            z2 = this.metadataReader.consume(prepareTagData(extractorInput), currentTimestampUs);
            long durationUs = this.metadataReader.getDurationUs();
            if (durationUs != -9223372036854775807L) {
                this.extractorOutput.seekMap(new SeekMap.Unseekable(durationUs));
                this.outputSeekMap = true;
            }
        } else {
            extractorInput.skipFully(this.tagDataSize);
            z = false;
            if (!this.outputFirstSample && z2) {
                this.outputFirstSample = true;
                this.mediaTagTimestampOffsetUs = this.metadataReader.getDurationUs() != -9223372036854775807L ? -this.tagTimestampUs : 0L;
            }
            this.bytesToNextTagHeader = 4;
            this.state = 2;
            return z;
        }
        z = true;
        if (!this.outputFirstSample) {
            this.outputFirstSample = true;
            this.mediaTagTimestampOffsetUs = this.metadataReader.getDurationUs() != -9223372036854775807L ? -this.tagTimestampUs : 0L;
        }
        this.bytesToNextTagHeader = 4;
        this.state = 2;
        return z;
    }

    private ParsableByteArray prepareTagData(ExtractorInput extractorInput) throws IOException, InterruptedException {
        if (this.tagDataSize > this.tagData.capacity()) {
            ParsableByteArray parsableByteArray = this.tagData;
            parsableByteArray.reset(new byte[Math.max(parsableByteArray.capacity() * 2, this.tagDataSize)], 0);
        } else {
            this.tagData.setPosition(0);
        }
        this.tagData.setLimit(this.tagDataSize);
        extractorInput.readFully(this.tagData.data, 0, this.tagDataSize);
        return this.tagData;
    }

    private void ensureReadyForMediaOutput() {
        if (this.outputSeekMap) {
            return;
        }
        this.extractorOutput.seekMap(new SeekMap.Unseekable(-9223372036854775807L));
        this.outputSeekMap = true;
    }

    private long getCurrentTimestampUs() {
        if (this.outputFirstSample) {
            return this.mediaTagTimestampOffsetUs + this.tagTimestampUs;
        }
        if (this.metadataReader.getDurationUs() == -9223372036854775807L) {
            return 0L;
        }
        return this.tagTimestampUs;
    }
}
