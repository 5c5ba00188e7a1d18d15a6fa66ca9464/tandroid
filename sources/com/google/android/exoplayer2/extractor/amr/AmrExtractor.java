package com.google.android.exoplayer2.extractor.amr;

import android.net.Uri;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.extractor.ConstantBitrateSeekMap;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.upstream.DataReader;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import java.io.EOFException;
import java.util.Arrays;
import java.util.Map;

/* loaded from: classes.dex */
public final class AmrExtractor implements Extractor {
    private static final int MAX_FRAME_SIZE_BYTES;
    private static final int[] frameSizeBytesByTypeWb;
    private int currentSampleBytesRemaining;
    private int currentSampleSize;
    private long currentSampleTimeUs;
    private ExtractorOutput extractorOutput;
    private long firstSamplePosition;
    private int firstSampleSize;
    private final int flags;
    private boolean hasOutputFormat;
    private boolean hasOutputSeekMap;
    private boolean isWideBand;
    private int numSamplesWithSameSize;
    private final byte[] scratch;
    private SeekMap seekMap;
    private long timeOffsetUs;
    private TrackOutput trackOutput;
    public static final ExtractorsFactory FACTORY = new ExtractorsFactory() { // from class: com.google.android.exoplayer2.extractor.amr.AmrExtractor$$ExternalSyntheticLambda0
        @Override // com.google.android.exoplayer2.extractor.ExtractorsFactory
        public final Extractor[] createExtractors() {
            Extractor[] lambda$static$0;
            lambda$static$0 = AmrExtractor.lambda$static$0();
            return lambda$static$0;
        }

        @Override // com.google.android.exoplayer2.extractor.ExtractorsFactory
        public /* synthetic */ Extractor[] createExtractors(Uri uri, Map map) {
            Extractor[] createExtractors;
            createExtractors = createExtractors();
            return createExtractors;
        }
    };
    private static final int[] frameSizeBytesByTypeNb = {13, 14, 16, 18, 20, 21, 27, 32, 6, 7, 6, 6, 1, 1, 1, 1};
    private static final byte[] amrSignatureNb = Util.getUtf8Bytes("#!AMR\n");
    private static final byte[] amrSignatureWb = Util.getUtf8Bytes("#!AMR-WB\n");

    static {
        int[] iArr = {18, 24, 33, 37, 41, 47, 51, 59, 61, 6, 1, 1, 1, 1, 1, 1};
        frameSizeBytesByTypeWb = iArr;
        MAX_FRAME_SIZE_BYTES = iArr[8];
    }

    public AmrExtractor() {
        this(0);
    }

    public AmrExtractor(int i) {
        this.flags = (i & 2) != 0 ? i | 1 : i;
        this.scratch = new byte[1];
        this.firstSampleSize = -1;
    }

    private void assertInitialized() {
        Assertions.checkStateNotNull(this.trackOutput);
        Util.castNonNull(this.extractorOutput);
    }

    private static int getBitrateFromFrameSize(int i, long j) {
        return (int) ((i * 8000000) / j);
    }

    private SeekMap getConstantBitrateSeekMap(long j, boolean z) {
        return new ConstantBitrateSeekMap(j, this.firstSamplePosition, getBitrateFromFrameSize(this.firstSampleSize, 20000L), this.firstSampleSize, z);
    }

    private int getFrameSizeInBytes(int i) {
        if (isValidFrameType(i)) {
            return this.isWideBand ? frameSizeBytesByTypeWb[i] : frameSizeBytesByTypeNb[i];
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Illegal AMR ");
        sb.append(this.isWideBand ? "WB" : "NB");
        sb.append(" frame type ");
        sb.append(i);
        throw ParserException.createForMalformedContainer(sb.toString(), null);
    }

    private boolean isNarrowBandValidFrameType(int i) {
        return !this.isWideBand && (i < 12 || i > 14);
    }

    private boolean isValidFrameType(int i) {
        return i >= 0 && i <= 15 && (isWideBandValidFrameType(i) || isNarrowBandValidFrameType(i));
    }

    private boolean isWideBandValidFrameType(int i) {
        return this.isWideBand && (i < 10 || i > 13);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Extractor[] lambda$static$0() {
        return new Extractor[]{new AmrExtractor()};
    }

    private void maybeOutputFormat() {
        if (this.hasOutputFormat) {
            return;
        }
        this.hasOutputFormat = true;
        boolean z = this.isWideBand;
        this.trackOutput.format(new Format.Builder().setSampleMimeType(z ? "audio/amr-wb" : "audio/3gpp").setMaxInputSize(MAX_FRAME_SIZE_BYTES).setChannelCount(1).setSampleRate(z ? 16000 : 8000).build());
    }

    private void maybeOutputSeekMap(long j, int i) {
        SeekMap unseekable;
        int i2;
        if (this.hasOutputSeekMap) {
            return;
        }
        int i3 = this.flags;
        if ((i3 & 1) == 0 || j == -1 || !((i2 = this.firstSampleSize) == -1 || i2 == this.currentSampleSize)) {
            unseekable = new SeekMap.Unseekable(-9223372036854775807L);
        } else if (this.numSamplesWithSameSize < 20 && i != -1) {
            return;
        } else {
            unseekable = getConstantBitrateSeekMap(j, (i3 & 2) != 0);
        }
        this.seekMap = unseekable;
        this.extractorOutput.seekMap(unseekable);
        this.hasOutputSeekMap = true;
    }

    private static boolean peekAmrSignature(ExtractorInput extractorInput, byte[] bArr) {
        extractorInput.resetPeekPosition();
        byte[] bArr2 = new byte[bArr.length];
        extractorInput.peekFully(bArr2, 0, bArr.length);
        return Arrays.equals(bArr2, bArr);
    }

    private int peekNextSampleSize(ExtractorInput extractorInput) {
        extractorInput.resetPeekPosition();
        extractorInput.peekFully(this.scratch, 0, 1);
        byte b = this.scratch[0];
        if ((b & 131) <= 0) {
            return getFrameSizeInBytes((b >> 3) & 15);
        }
        throw ParserException.createForMalformedContainer("Invalid padding bits for frame header " + ((int) b), null);
    }

    private boolean readAmrHeader(ExtractorInput extractorInput) {
        int length;
        byte[] bArr = amrSignatureNb;
        if (peekAmrSignature(extractorInput, bArr)) {
            this.isWideBand = false;
            length = bArr.length;
        } else {
            byte[] bArr2 = amrSignatureWb;
            if (!peekAmrSignature(extractorInput, bArr2)) {
                return false;
            }
            this.isWideBand = true;
            length = bArr2.length;
        }
        extractorInput.skipFully(length);
        return true;
    }

    private int readSample(ExtractorInput extractorInput) {
        if (this.currentSampleBytesRemaining == 0) {
            try {
                int peekNextSampleSize = peekNextSampleSize(extractorInput);
                this.currentSampleSize = peekNextSampleSize;
                this.currentSampleBytesRemaining = peekNextSampleSize;
                if (this.firstSampleSize == -1) {
                    this.firstSamplePosition = extractorInput.getPosition();
                    this.firstSampleSize = this.currentSampleSize;
                }
                if (this.firstSampleSize == this.currentSampleSize) {
                    this.numSamplesWithSameSize++;
                }
            } catch (EOFException unused) {
                return -1;
            }
        }
        int sampleData = this.trackOutput.sampleData((DataReader) extractorInput, this.currentSampleBytesRemaining, true);
        if (sampleData == -1) {
            return -1;
        }
        int i = this.currentSampleBytesRemaining - sampleData;
        this.currentSampleBytesRemaining = i;
        if (i > 0) {
            return 0;
        }
        this.trackOutput.sampleMetadata(this.timeOffsetUs + this.currentSampleTimeUs, 1, this.currentSampleSize, 0, null);
        this.currentSampleTimeUs += 20000;
        return 0;
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public void init(ExtractorOutput extractorOutput) {
        this.extractorOutput = extractorOutput;
        this.trackOutput = extractorOutput.track(0, 1);
        extractorOutput.endTracks();
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public int read(ExtractorInput extractorInput, PositionHolder positionHolder) {
        assertInitialized();
        if (extractorInput.getPosition() == 0 && !readAmrHeader(extractorInput)) {
            throw ParserException.createForMalformedContainer("Could not find AMR header.", null);
        }
        maybeOutputFormat();
        int readSample = readSample(extractorInput);
        maybeOutputSeekMap(extractorInput.getLength(), readSample);
        return readSample;
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public void release() {
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public void seek(long j, long j2) {
        this.currentSampleTimeUs = 0L;
        this.currentSampleSize = 0;
        this.currentSampleBytesRemaining = 0;
        if (j != 0) {
            SeekMap seekMap = this.seekMap;
            if (seekMap instanceof ConstantBitrateSeekMap) {
                this.timeOffsetUs = ((ConstantBitrateSeekMap) seekMap).getTimeUsAtPosition(j);
                return;
            }
        }
        this.timeOffsetUs = 0L;
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public boolean sniff(ExtractorInput extractorInput) {
        return readAmrHeader(extractorInput);
    }
}
