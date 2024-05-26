package com.google.android.exoplayer2.extractor.ts;

import android.util.Pair;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.extractor.ts.TsPayloadReader;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.NalUnitUtil;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.util.Arrays;
import java.util.Collections;
/* loaded from: classes.dex */
public final class H262Reader implements ElementaryStreamReader {
    private static final double[] FRAME_RATE_VALUES = {23.976023976023978d, 24.0d, 25.0d, 29.97002997002997d, 30.0d, 50.0d, 59.94005994005994d, 60.0d};
    private final CsdBuffer csdBuffer;
    private String formatId;
    private long frameDurationUs;
    private boolean hasOutputFormat;
    private TrackOutput output;
    private long pesTimeUs;
    private final boolean[] prefixFlags;
    private boolean sampleHasPicture;
    private boolean sampleIsKeyframe;
    private long samplePosition;
    private long sampleTimeUs;
    private boolean startedFirstSample;
    private long totalBytesWritten;
    private final NalUnitTargetBuffer userData;
    private final ParsableByteArray userDataParsable;
    private final UserDataReader userDataReader;

    @Override // com.google.android.exoplayer2.extractor.ts.ElementaryStreamReader
    public void packetFinished() {
    }

    public H262Reader() {
        this(null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public H262Reader(UserDataReader userDataReader) {
        this.userDataReader = userDataReader;
        this.prefixFlags = new boolean[4];
        this.csdBuffer = new CsdBuffer(128);
        if (userDataReader != null) {
            this.userData = new NalUnitTargetBuffer(178, 128);
            this.userDataParsable = new ParsableByteArray();
        } else {
            this.userData = null;
            this.userDataParsable = null;
        }
        this.pesTimeUs = -9223372036854775807L;
        this.sampleTimeUs = -9223372036854775807L;
    }

    @Override // com.google.android.exoplayer2.extractor.ts.ElementaryStreamReader
    public void seek() {
        NalUnitUtil.clearPrefixFlags(this.prefixFlags);
        this.csdBuffer.reset();
        NalUnitTargetBuffer nalUnitTargetBuffer = this.userData;
        if (nalUnitTargetBuffer != null) {
            nalUnitTargetBuffer.reset();
        }
        this.totalBytesWritten = 0L;
        this.startedFirstSample = false;
        this.pesTimeUs = -9223372036854775807L;
        this.sampleTimeUs = -9223372036854775807L;
    }

    @Override // com.google.android.exoplayer2.extractor.ts.ElementaryStreamReader
    public void createTracks(ExtractorOutput extractorOutput, TsPayloadReader.TrackIdGenerator trackIdGenerator) {
        trackIdGenerator.generateNewId();
        this.formatId = trackIdGenerator.getFormatId();
        this.output = extractorOutput.track(trackIdGenerator.getTrackId(), 2);
        UserDataReader userDataReader = this.userDataReader;
        if (userDataReader != null) {
            userDataReader.createTracks(extractorOutput, trackIdGenerator);
        }
    }

    @Override // com.google.android.exoplayer2.extractor.ts.ElementaryStreamReader
    public void packetStarted(long j, int i) {
        this.pesTimeUs = j;
    }

    /* JADX WARN: Removed duplicated region for block: B:59:0x012c  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0142  */
    @Override // com.google.android.exoplayer2.extractor.ts.ElementaryStreamReader
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void consume(ParsableByteArray parsableByteArray) {
        int i;
        long j;
        int i2;
        Assertions.checkStateNotNull(this.output);
        int position = parsableByteArray.getPosition();
        int limit = parsableByteArray.limit();
        byte[] data = parsableByteArray.getData();
        this.totalBytesWritten += parsableByteArray.bytesLeft();
        this.output.sampleData(parsableByteArray, parsableByteArray.bytesLeft());
        while (true) {
            int findNalUnit = NalUnitUtil.findNalUnit(data, position, limit, this.prefixFlags);
            if (findNalUnit == limit) {
                break;
            }
            int i3 = findNalUnit + 3;
            int i4 = parsableByteArray.getData()[i3] & 255;
            int i5 = findNalUnit - position;
            if (!this.hasOutputFormat) {
                if (i5 > 0) {
                    this.csdBuffer.onData(data, position, findNalUnit);
                }
                if (this.csdBuffer.onStartCode(i4, i5 < 0 ? -i5 : 0)) {
                    Pair<Format, Long> parseCsdBuffer = parseCsdBuffer(this.csdBuffer, (String) Assertions.checkNotNull(this.formatId));
                    this.output.format((Format) parseCsdBuffer.first);
                    this.frameDurationUs = ((Long) parseCsdBuffer.second).longValue();
                    this.hasOutputFormat = true;
                }
            }
            NalUnitTargetBuffer nalUnitTargetBuffer = this.userData;
            if (nalUnitTargetBuffer != null) {
                if (i5 > 0) {
                    nalUnitTargetBuffer.appendToNalUnit(data, position, findNalUnit);
                    i2 = 0;
                } else {
                    i2 = -i5;
                }
                if (this.userData.endNalUnit(i2)) {
                    NalUnitTargetBuffer nalUnitTargetBuffer2 = this.userData;
                    ((ParsableByteArray) Util.castNonNull(this.userDataParsable)).reset(this.userData.nalData, NalUnitUtil.unescapeStream(nalUnitTargetBuffer2.nalData, nalUnitTargetBuffer2.nalLength));
                    ((UserDataReader) Util.castNonNull(this.userDataReader)).consume(this.sampleTimeUs, this.userDataParsable);
                }
                if (i4 == 178 && parsableByteArray.getData()[findNalUnit + 2] == 1) {
                    this.userData.startNalUnit(i4);
                }
            }
            if (i4 == 0 || i4 == 179) {
                int i6 = limit - findNalUnit;
                if (this.sampleHasPicture && this.hasOutputFormat) {
                    long j2 = this.sampleTimeUs;
                    if (j2 != -9223372036854775807L) {
                        i = i4;
                        this.output.sampleMetadata(j2, this.sampleIsKeyframe ? 1 : 0, ((int) (this.totalBytesWritten - this.samplePosition)) - i6, i6, null);
                        if (this.startedFirstSample || this.sampleHasPicture) {
                            this.samplePosition = this.totalBytesWritten - i6;
                            j = this.pesTimeUs;
                            if (j == -9223372036854775807L) {
                                long j3 = this.sampleTimeUs;
                                j = j3 != -9223372036854775807L ? j3 + this.frameDurationUs : -9223372036854775807L;
                            }
                            this.sampleTimeUs = j;
                            this.sampleIsKeyframe = false;
                            this.pesTimeUs = -9223372036854775807L;
                            this.startedFirstSample = true;
                        }
                        this.sampleHasPicture = i == 0;
                    }
                }
                i = i4;
                if (this.startedFirstSample) {
                }
                this.samplePosition = this.totalBytesWritten - i6;
                j = this.pesTimeUs;
                if (j == -9223372036854775807L) {
                }
                this.sampleTimeUs = j;
                this.sampleIsKeyframe = false;
                this.pesTimeUs = -9223372036854775807L;
                this.startedFirstSample = true;
                this.sampleHasPicture = i == 0;
            } else if (i4 == 184) {
                this.sampleIsKeyframe = true;
            }
            position = i3;
        }
        if (!this.hasOutputFormat) {
            this.csdBuffer.onData(data, position, limit);
        }
        NalUnitTargetBuffer nalUnitTargetBuffer3 = this.userData;
        if (nalUnitTargetBuffer3 != null) {
            nalUnitTargetBuffer3.appendToNalUnit(data, position, limit);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x0076  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static Pair<Format, Long> parseCsdBuffer(CsdBuffer csdBuffer, String str) {
        float f;
        int i;
        float f2;
        int i2;
        byte[] copyOf = Arrays.copyOf(csdBuffer.data, csdBuffer.length);
        int i3 = copyOf[5] & 255;
        int i4 = ((copyOf[4] & 255) << 4) | (i3 >> 4);
        int i5 = ((i3 & 15) << 8) | (copyOf[6] & 255);
        int i6 = (copyOf[7] & 240) >> 4;
        if (i6 == 2) {
            f = i5 * 4;
            i = i4 * 3;
        } else if (i6 == 3) {
            f = i5 * 16;
            i = i4 * 9;
        } else if (i6 != 4) {
            f2 = 1.0f;
            Format build = new Format.Builder().setId(str).setSampleMimeType("video/mpeg2").setWidth(i4).setHeight(i5).setPixelWidthHeightRatio(f2).setInitializationData(Collections.singletonList(copyOf)).build();
            long j = 0;
            i2 = (copyOf[7] & 15) - 1;
            if (i2 >= 0) {
                double[] dArr = FRAME_RATE_VALUES;
                if (i2 < dArr.length) {
                    double d = dArr[i2];
                    int i7 = csdBuffer.sequenceExtensionPosition + 9;
                    int i8 = (copyOf[i7] & 96) >> 5;
                    int i9 = copyOf[i7] & 31;
                    if (i8 != i9) {
                        double d2 = i8;
                        Double.isNaN(d2);
                        double d3 = i9 + 1;
                        Double.isNaN(d3);
                        d *= (d2 + 1.0d) / d3;
                    }
                    j = (long) (1000000.0d / d);
                }
            }
            return Pair.create(build, Long.valueOf(j));
        } else {
            f = i5 * 121;
            i = i4 * 100;
        }
        f2 = f / i;
        Format build2 = new Format.Builder().setId(str).setSampleMimeType("video/mpeg2").setWidth(i4).setHeight(i5).setPixelWidthHeightRatio(f2).setInitializationData(Collections.singletonList(copyOf)).build();
        long j2 = 0;
        i2 = (copyOf[7] & 15) - 1;
        if (i2 >= 0) {
        }
        return Pair.create(build2, Long.valueOf(j2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class CsdBuffer {
        private static final byte[] START_CODE = {0, 0, 1};
        public byte[] data;
        private boolean isFilling;
        public int length;
        public int sequenceExtensionPosition;

        public CsdBuffer(int i) {
            this.data = new byte[i];
        }

        public void reset() {
            this.isFilling = false;
            this.length = 0;
            this.sequenceExtensionPosition = 0;
        }

        public boolean onStartCode(int i, int i2) {
            if (this.isFilling) {
                int i3 = this.length - i2;
                this.length = i3;
                if (this.sequenceExtensionPosition == 0 && i == 181) {
                    this.sequenceExtensionPosition = i3;
                } else {
                    this.isFilling = false;
                    return true;
                }
            } else if (i == 179) {
                this.isFilling = true;
            }
            byte[] bArr = START_CODE;
            onData(bArr, 0, bArr.length);
            return false;
        }

        public void onData(byte[] bArr, int i, int i2) {
            if (this.isFilling) {
                int i3 = i2 - i;
                byte[] bArr2 = this.data;
                int length = bArr2.length;
                int i4 = this.length;
                if (length < i4 + i3) {
                    this.data = Arrays.copyOf(bArr2, (i4 + i3) * 2);
                }
                System.arraycopy(bArr, i, this.data, this.length, i3);
                this.length += i3;
            }
        }
    }
}
