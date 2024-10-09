package com.google.android.exoplayer2.extractor.ts;

import android.util.SparseArray;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.extractor.ts.TsPayloadReader;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.CodecSpecificDataUtil;
import com.google.android.exoplayer2.util.NalUnitUtil;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.ParsableNalUnitBitArray;
import com.google.android.exoplayer2.util.Util;
import java.util.ArrayList;
import java.util.Arrays;
import org.telegram.messenger.MediaController;

/* loaded from: classes.dex */
public final class H264Reader implements ElementaryStreamReader {
    private final boolean allowNonIdrKeyframes;
    private final boolean detectAccessUnits;
    private String formatId;
    private boolean hasOutputFormat;
    private TrackOutput output;
    private boolean randomAccessIndicator;
    private SampleReader sampleReader;
    private final SeiReader seiReader;
    private long totalBytesWritten;
    private final boolean[] prefixFlags = new boolean[3];
    private final NalUnitTargetBuffer sps = new NalUnitTargetBuffer(7, 128);
    private final NalUnitTargetBuffer pps = new NalUnitTargetBuffer(8, 128);
    private final NalUnitTargetBuffer sei = new NalUnitTargetBuffer(6, 128);
    private long pesTimeUs = -9223372036854775807L;
    private final ParsableByteArray seiWrapper = new ParsableByteArray();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class SampleReader {
        private final boolean allowNonIdrKeyframes;
        private final ParsableNalUnitBitArray bitArray;
        private byte[] buffer;
        private int bufferLength;
        private final boolean detectAccessUnits;
        private boolean isFilling;
        private long nalUnitStartPosition;
        private long nalUnitTimeUs;
        private int nalUnitType;
        private final TrackOutput output;
        private SliceHeaderData previousSliceHeader;
        private boolean readingSample;
        private boolean sampleIsKeyframe;
        private long samplePosition;
        private long sampleTimeUs;
        private SliceHeaderData sliceHeader;
        private final SparseArray sps = new SparseArray();
        private final SparseArray pps = new SparseArray();

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static final class SliceHeaderData {
            private boolean bottomFieldFlag;
            private boolean bottomFieldFlagPresent;
            private int deltaPicOrderCnt0;
            private int deltaPicOrderCnt1;
            private int deltaPicOrderCntBottom;
            private boolean fieldPicFlag;
            private int frameNum;
            private boolean hasSliceType;
            private boolean idrPicFlag;
            private int idrPicId;
            private boolean isComplete;
            private int nalRefIdc;
            private int picOrderCntLsb;
            private int picParameterSetId;
            private int sliceType;
            private NalUnitUtil.SpsData spsData;

            private SliceHeaderData() {
            }

            /* JADX INFO: Access modifiers changed from: private */
            public boolean isFirstVclNalUnitOfPicture(SliceHeaderData sliceHeaderData) {
                int i;
                int i2;
                int i3;
                boolean z;
                if (!this.isComplete) {
                    return false;
                }
                if (!sliceHeaderData.isComplete) {
                    return true;
                }
                NalUnitUtil.SpsData spsData = (NalUnitUtil.SpsData) Assertions.checkStateNotNull(this.spsData);
                NalUnitUtil.SpsData spsData2 = (NalUnitUtil.SpsData) Assertions.checkStateNotNull(sliceHeaderData.spsData);
                return (this.frameNum == sliceHeaderData.frameNum && this.picParameterSetId == sliceHeaderData.picParameterSetId && this.fieldPicFlag == sliceHeaderData.fieldPicFlag && (!this.bottomFieldFlagPresent || !sliceHeaderData.bottomFieldFlagPresent || this.bottomFieldFlag == sliceHeaderData.bottomFieldFlag) && (((i = this.nalRefIdc) == (i2 = sliceHeaderData.nalRefIdc) || (i != 0 && i2 != 0)) && (((i3 = spsData.picOrderCountType) != 0 || spsData2.picOrderCountType != 0 || (this.picOrderCntLsb == sliceHeaderData.picOrderCntLsb && this.deltaPicOrderCntBottom == sliceHeaderData.deltaPicOrderCntBottom)) && ((i3 != 1 || spsData2.picOrderCountType != 1 || (this.deltaPicOrderCnt0 == sliceHeaderData.deltaPicOrderCnt0 && this.deltaPicOrderCnt1 == sliceHeaderData.deltaPicOrderCnt1)) && (z = this.idrPicFlag) == sliceHeaderData.idrPicFlag && (!z || this.idrPicId == sliceHeaderData.idrPicId))))) ? false : true;
            }

            public void clear() {
                this.hasSliceType = false;
                this.isComplete = false;
            }

            public boolean isISlice() {
                int i;
                return this.hasSliceType && ((i = this.sliceType) == 7 || i == 2);
            }

            public void setAll(NalUnitUtil.SpsData spsData, int i, int i2, int i3, int i4, boolean z, boolean z2, boolean z3, boolean z4, int i5, int i6, int i7, int i8, int i9) {
                this.spsData = spsData;
                this.nalRefIdc = i;
                this.sliceType = i2;
                this.frameNum = i3;
                this.picParameterSetId = i4;
                this.fieldPicFlag = z;
                this.bottomFieldFlagPresent = z2;
                this.bottomFieldFlag = z3;
                this.idrPicFlag = z4;
                this.idrPicId = i5;
                this.picOrderCntLsb = i6;
                this.deltaPicOrderCntBottom = i7;
                this.deltaPicOrderCnt0 = i8;
                this.deltaPicOrderCnt1 = i9;
                this.isComplete = true;
                this.hasSliceType = true;
            }

            public void setSliceType(int i) {
                this.sliceType = i;
                this.hasSliceType = true;
            }
        }

        public SampleReader(TrackOutput trackOutput, boolean z, boolean z2) {
            this.output = trackOutput;
            this.allowNonIdrKeyframes = z;
            this.detectAccessUnits = z2;
            this.previousSliceHeader = new SliceHeaderData();
            this.sliceHeader = new SliceHeaderData();
            byte[] bArr = new byte[128];
            this.buffer = bArr;
            this.bitArray = new ParsableNalUnitBitArray(bArr, 0, 0);
            reset();
        }

        private void outputSample(int i) {
            long j = this.sampleTimeUs;
            if (j == -9223372036854775807L) {
                return;
            }
            boolean z = this.sampleIsKeyframe;
            this.output.sampleMetadata(j, z ? 1 : 0, (int) (this.nalUnitStartPosition - this.samplePosition), i, null);
        }

        /* JADX WARN: Removed duplicated region for block: B:50:0x00fc  */
        /* JADX WARN: Removed duplicated region for block: B:52:0x0103  */
        /* JADX WARN: Removed duplicated region for block: B:58:0x011b  */
        /* JADX WARN: Removed duplicated region for block: B:74:0x0151  */
        /* JADX WARN: Removed duplicated region for block: B:89:0x0115  */
        /* JADX WARN: Removed duplicated region for block: B:90:0x00ff  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void appendToNalUnit(byte[] bArr, int i, int i2) {
            boolean z;
            boolean z2;
            boolean z3;
            boolean z4;
            int i3;
            int i4;
            int i5;
            int i6;
            int i7;
            int readSignedExpGolombCodedInt;
            if (this.isFilling) {
                int i8 = i2 - i;
                byte[] bArr2 = this.buffer;
                int length = bArr2.length;
                int i9 = this.bufferLength + i8;
                if (length < i9) {
                    this.buffer = Arrays.copyOf(bArr2, i9 * 2);
                }
                System.arraycopy(bArr, i, this.buffer, this.bufferLength, i8);
                int i10 = this.bufferLength + i8;
                this.bufferLength = i10;
                this.bitArray.reset(this.buffer, 0, i10);
                if (this.bitArray.canReadBits(8)) {
                    this.bitArray.skipBit();
                    int readBits = this.bitArray.readBits(2);
                    this.bitArray.skipBits(5);
                    if (this.bitArray.canReadExpGolombCodedNum()) {
                        this.bitArray.readUnsignedExpGolombCodedInt();
                        if (this.bitArray.canReadExpGolombCodedNum()) {
                            int readUnsignedExpGolombCodedInt = this.bitArray.readUnsignedExpGolombCodedInt();
                            if (!this.detectAccessUnits) {
                                this.isFilling = false;
                                this.sliceHeader.setSliceType(readUnsignedExpGolombCodedInt);
                                return;
                            }
                            if (this.bitArray.canReadExpGolombCodedNum()) {
                                int readUnsignedExpGolombCodedInt2 = this.bitArray.readUnsignedExpGolombCodedInt();
                                if (this.pps.indexOfKey(readUnsignedExpGolombCodedInt2) < 0) {
                                    this.isFilling = false;
                                    return;
                                }
                                NalUnitUtil.PpsData ppsData = (NalUnitUtil.PpsData) this.pps.get(readUnsignedExpGolombCodedInt2);
                                NalUnitUtil.SpsData spsData = (NalUnitUtil.SpsData) this.sps.get(ppsData.seqParameterSetId);
                                if (spsData.separateColorPlaneFlag) {
                                    if (!this.bitArray.canReadBits(2)) {
                                        return;
                                    } else {
                                        this.bitArray.skipBits(2);
                                    }
                                }
                                if (this.bitArray.canReadBits(spsData.frameNumLength)) {
                                    int readBits2 = this.bitArray.readBits(spsData.frameNumLength);
                                    if (spsData.frameMbsOnlyFlag) {
                                        z = false;
                                    } else {
                                        if (!this.bitArray.canReadBits(1)) {
                                            return;
                                        }
                                        boolean readBit = this.bitArray.readBit();
                                        if (readBit) {
                                            if (this.bitArray.canReadBits(1)) {
                                                z = readBit;
                                                z3 = this.bitArray.readBit();
                                                z2 = true;
                                                z4 = this.nalUnitType != 5;
                                                if (z4) {
                                                    i3 = 0;
                                                } else if (!this.bitArray.canReadExpGolombCodedNum()) {
                                                    return;
                                                } else {
                                                    i3 = this.bitArray.readUnsignedExpGolombCodedInt();
                                                }
                                                i4 = spsData.picOrderCountType;
                                                if (i4 != 0) {
                                                    if (!this.bitArray.canReadBits(spsData.picOrderCntLsbLength)) {
                                                        return;
                                                    }
                                                    int readBits3 = this.bitArray.readBits(spsData.picOrderCntLsbLength);
                                                    if (ppsData.bottomFieldPicOrderInFramePresentFlag && !z) {
                                                        if (this.bitArray.canReadExpGolombCodedNum()) {
                                                            i7 = this.bitArray.readSignedExpGolombCodedInt();
                                                            i5 = readBits3;
                                                            i6 = 0;
                                                            readSignedExpGolombCodedInt = 0;
                                                            this.sliceHeader.setAll(spsData, readBits, readUnsignedExpGolombCodedInt, readBits2, readUnsignedExpGolombCodedInt2, z, z2, z3, z4, i3, i5, i7, i6, readSignedExpGolombCodedInt);
                                                            this.isFilling = false;
                                                        }
                                                        return;
                                                    }
                                                    i5 = readBits3;
                                                } else {
                                                    if (i4 == 1 && !spsData.deltaPicOrderAlwaysZeroFlag) {
                                                        if (this.bitArray.canReadExpGolombCodedNum()) {
                                                            int readSignedExpGolombCodedInt2 = this.bitArray.readSignedExpGolombCodedInt();
                                                            if (!ppsData.bottomFieldPicOrderInFramePresentFlag || z) {
                                                                i6 = readSignedExpGolombCodedInt2;
                                                                i5 = 0;
                                                                i7 = 0;
                                                                readSignedExpGolombCodedInt = 0;
                                                                this.sliceHeader.setAll(spsData, readBits, readUnsignedExpGolombCodedInt, readBits2, readUnsignedExpGolombCodedInt2, z, z2, z3, z4, i3, i5, i7, i6, readSignedExpGolombCodedInt);
                                                                this.isFilling = false;
                                                            }
                                                            if (this.bitArray.canReadExpGolombCodedNum()) {
                                                                readSignedExpGolombCodedInt = this.bitArray.readSignedExpGolombCodedInt();
                                                                i6 = readSignedExpGolombCodedInt2;
                                                                i5 = 0;
                                                                i7 = 0;
                                                                this.sliceHeader.setAll(spsData, readBits, readUnsignedExpGolombCodedInt, readBits2, readUnsignedExpGolombCodedInt2, z, z2, z3, z4, i3, i5, i7, i6, readSignedExpGolombCodedInt);
                                                                this.isFilling = false;
                                                            }
                                                            return;
                                                        }
                                                        return;
                                                    }
                                                    i5 = 0;
                                                }
                                                i7 = 0;
                                                i6 = 0;
                                                readSignedExpGolombCodedInt = 0;
                                                this.sliceHeader.setAll(spsData, readBits, readUnsignedExpGolombCodedInt, readBits2, readUnsignedExpGolombCodedInt2, z, z2, z3, z4, i3, i5, i7, i6, readSignedExpGolombCodedInt);
                                                this.isFilling = false;
                                            }
                                            return;
                                        }
                                        z = readBit;
                                    }
                                    z2 = false;
                                    z3 = false;
                                    if (this.nalUnitType != 5) {
                                    }
                                    if (z4) {
                                    }
                                    i4 = spsData.picOrderCountType;
                                    if (i4 != 0) {
                                    }
                                    i7 = 0;
                                    i6 = 0;
                                    readSignedExpGolombCodedInt = 0;
                                    this.sliceHeader.setAll(spsData, readBits, readUnsignedExpGolombCodedInt, readBits2, readUnsignedExpGolombCodedInt2, z, z2, z3, z4, i3, i5, i7, i6, readSignedExpGolombCodedInt);
                                    this.isFilling = false;
                                }
                            }
                        }
                    }
                }
            }
        }

        public boolean endNalUnit(long j, int i, boolean z, boolean z2) {
            boolean z3 = false;
            if (this.nalUnitType == 9 || (this.detectAccessUnits && this.sliceHeader.isFirstVclNalUnitOfPicture(this.previousSliceHeader))) {
                if (z && this.readingSample) {
                    outputSample(i + ((int) (j - this.nalUnitStartPosition)));
                }
                this.samplePosition = this.nalUnitStartPosition;
                this.sampleTimeUs = this.nalUnitTimeUs;
                this.sampleIsKeyframe = false;
                this.readingSample = true;
            }
            if (this.allowNonIdrKeyframes) {
                z2 = this.sliceHeader.isISlice();
            }
            boolean z4 = this.sampleIsKeyframe;
            int i2 = this.nalUnitType;
            if (i2 == 5 || (z2 && i2 == 1)) {
                z3 = true;
            }
            boolean z5 = z4 | z3;
            this.sampleIsKeyframe = z5;
            return z5;
        }

        public boolean needsSpsPps() {
            return this.detectAccessUnits;
        }

        public void putPps(NalUnitUtil.PpsData ppsData) {
            this.pps.append(ppsData.picParameterSetId, ppsData);
        }

        public void putSps(NalUnitUtil.SpsData spsData) {
            this.sps.append(spsData.seqParameterSetId, spsData);
        }

        public void reset() {
            this.isFilling = false;
            this.readingSample = false;
            this.sliceHeader.clear();
        }

        public void startNalUnit(long j, int i, long j2) {
            this.nalUnitType = i;
            this.nalUnitTimeUs = j2;
            this.nalUnitStartPosition = j;
            if (!this.allowNonIdrKeyframes || i != 1) {
                if (!this.detectAccessUnits) {
                    return;
                }
                if (i != 5 && i != 1 && i != 2) {
                    return;
                }
            }
            SliceHeaderData sliceHeaderData = this.previousSliceHeader;
            this.previousSliceHeader = this.sliceHeader;
            this.sliceHeader = sliceHeaderData;
            sliceHeaderData.clear();
            this.bufferLength = 0;
            this.isFilling = true;
        }
    }

    public H264Reader(SeiReader seiReader, boolean z, boolean z2) {
        this.seiReader = seiReader;
        this.allowNonIdrKeyframes = z;
        this.detectAccessUnits = z2;
    }

    private void assertTracksCreated() {
        Assertions.checkStateNotNull(this.output);
        Util.castNonNull(this.sampleReader);
    }

    private void endNalUnit(long j, int i, int i2, long j2) {
        NalUnitTargetBuffer nalUnitTargetBuffer;
        if (!this.hasOutputFormat || this.sampleReader.needsSpsPps()) {
            this.sps.endNalUnit(i2);
            this.pps.endNalUnit(i2);
            if (this.hasOutputFormat) {
                if (this.sps.isCompleted()) {
                    NalUnitTargetBuffer nalUnitTargetBuffer2 = this.sps;
                    this.sampleReader.putSps(NalUnitUtil.parseSpsNalUnit(nalUnitTargetBuffer2.nalData, 3, nalUnitTargetBuffer2.nalLength));
                    nalUnitTargetBuffer = this.sps;
                } else if (this.pps.isCompleted()) {
                    NalUnitTargetBuffer nalUnitTargetBuffer3 = this.pps;
                    this.sampleReader.putPps(NalUnitUtil.parsePpsNalUnit(nalUnitTargetBuffer3.nalData, 3, nalUnitTargetBuffer3.nalLength));
                    nalUnitTargetBuffer = this.pps;
                }
            } else if (this.sps.isCompleted() && this.pps.isCompleted()) {
                ArrayList arrayList = new ArrayList();
                NalUnitTargetBuffer nalUnitTargetBuffer4 = this.sps;
                arrayList.add(Arrays.copyOf(nalUnitTargetBuffer4.nalData, nalUnitTargetBuffer4.nalLength));
                NalUnitTargetBuffer nalUnitTargetBuffer5 = this.pps;
                arrayList.add(Arrays.copyOf(nalUnitTargetBuffer5.nalData, nalUnitTargetBuffer5.nalLength));
                NalUnitTargetBuffer nalUnitTargetBuffer6 = this.sps;
                NalUnitUtil.SpsData parseSpsNalUnit = NalUnitUtil.parseSpsNalUnit(nalUnitTargetBuffer6.nalData, 3, nalUnitTargetBuffer6.nalLength);
                NalUnitTargetBuffer nalUnitTargetBuffer7 = this.pps;
                NalUnitUtil.PpsData parsePpsNalUnit = NalUnitUtil.parsePpsNalUnit(nalUnitTargetBuffer7.nalData, 3, nalUnitTargetBuffer7.nalLength);
                this.output.format(new Format.Builder().setId(this.formatId).setSampleMimeType(MediaController.VIDEO_MIME_TYPE).setCodecs(CodecSpecificDataUtil.buildAvcCodecString(parseSpsNalUnit.profileIdc, parseSpsNalUnit.constraintsFlagsAndReservedZero2Bits, parseSpsNalUnit.levelIdc)).setWidth(parseSpsNalUnit.width).setHeight(parseSpsNalUnit.height).setPixelWidthHeightRatio(parseSpsNalUnit.pixelWidthHeightRatio).setInitializationData(arrayList).build());
                this.hasOutputFormat = true;
                this.sampleReader.putSps(parseSpsNalUnit);
                this.sampleReader.putPps(parsePpsNalUnit);
                this.sps.reset();
                nalUnitTargetBuffer = this.pps;
            }
            nalUnitTargetBuffer.reset();
        }
        if (this.sei.endNalUnit(i2)) {
            NalUnitTargetBuffer nalUnitTargetBuffer8 = this.sei;
            this.seiWrapper.reset(this.sei.nalData, NalUnitUtil.unescapeStream(nalUnitTargetBuffer8.nalData, nalUnitTargetBuffer8.nalLength));
            this.seiWrapper.setPosition(4);
            this.seiReader.consume(j2, this.seiWrapper);
        }
        if (this.sampleReader.endNalUnit(j, i, this.hasOutputFormat, this.randomAccessIndicator)) {
            this.randomAccessIndicator = false;
        }
    }

    private void nalUnitData(byte[] bArr, int i, int i2) {
        if (!this.hasOutputFormat || this.sampleReader.needsSpsPps()) {
            this.sps.appendToNalUnit(bArr, i, i2);
            this.pps.appendToNalUnit(bArr, i, i2);
        }
        this.sei.appendToNalUnit(bArr, i, i2);
        this.sampleReader.appendToNalUnit(bArr, i, i2);
    }

    private void startNalUnit(long j, int i, long j2) {
        if (!this.hasOutputFormat || this.sampleReader.needsSpsPps()) {
            this.sps.startNalUnit(i);
            this.pps.startNalUnit(i);
        }
        this.sei.startNalUnit(i);
        this.sampleReader.startNalUnit(j, i, j2);
    }

    @Override // com.google.android.exoplayer2.extractor.ts.ElementaryStreamReader
    public void consume(ParsableByteArray parsableByteArray) {
        assertTracksCreated();
        int position = parsableByteArray.getPosition();
        int limit = parsableByteArray.limit();
        byte[] data = parsableByteArray.getData();
        this.totalBytesWritten += parsableByteArray.bytesLeft();
        this.output.sampleData(parsableByteArray, parsableByteArray.bytesLeft());
        while (true) {
            int findNalUnit = NalUnitUtil.findNalUnit(data, position, limit, this.prefixFlags);
            if (findNalUnit == limit) {
                nalUnitData(data, position, limit);
                return;
            }
            int nalUnitType = NalUnitUtil.getNalUnitType(data, findNalUnit);
            int i = findNalUnit - position;
            if (i > 0) {
                nalUnitData(data, position, findNalUnit);
            }
            int i2 = limit - findNalUnit;
            long j = this.totalBytesWritten - i2;
            endNalUnit(j, i2, i < 0 ? -i : 0, this.pesTimeUs);
            startNalUnit(j, nalUnitType, this.pesTimeUs);
            position = findNalUnit + 3;
        }
    }

    @Override // com.google.android.exoplayer2.extractor.ts.ElementaryStreamReader
    public void createTracks(ExtractorOutput extractorOutput, TsPayloadReader.TrackIdGenerator trackIdGenerator) {
        trackIdGenerator.generateNewId();
        this.formatId = trackIdGenerator.getFormatId();
        TrackOutput track = extractorOutput.track(trackIdGenerator.getTrackId(), 2);
        this.output = track;
        this.sampleReader = new SampleReader(track, this.allowNonIdrKeyframes, this.detectAccessUnits);
        this.seiReader.createTracks(extractorOutput, trackIdGenerator);
    }

    @Override // com.google.android.exoplayer2.extractor.ts.ElementaryStreamReader
    public void packetFinished() {
    }

    @Override // com.google.android.exoplayer2.extractor.ts.ElementaryStreamReader
    public void packetStarted(long j, int i) {
        if (j != -9223372036854775807L) {
            this.pesTimeUs = j;
        }
        this.randomAccessIndicator |= (i & 2) != 0;
    }

    @Override // com.google.android.exoplayer2.extractor.ts.ElementaryStreamReader
    public void seek() {
        this.totalBytesWritten = 0L;
        this.randomAccessIndicator = false;
        this.pesTimeUs = -9223372036854775807L;
        NalUnitUtil.clearPrefixFlags(this.prefixFlags);
        this.sps.reset();
        this.pps.reset();
        this.sei.reset();
        SampleReader sampleReader = this.sampleReader;
        if (sampleReader != null) {
            sampleReader.reset();
        }
    }
}
