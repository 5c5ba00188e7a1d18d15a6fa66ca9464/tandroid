package com.google.android.exoplayer2.extractor.mkv;

import android.net.Uri;
import android.util.Pair;
import android.util.SparseArray;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.audio.AacUtil;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.ChunkIndex;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.extractor.TrueHdSampleRechunker;
import com.google.android.exoplayer2.upstream.DataReader;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.LongArray;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.NalUnitUtil;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.AvcConfig;
import com.google.android.exoplayer2.video.ColorInfo;
import com.google.android.exoplayer2.video.DolbyVisionConfig;
import com.google.android.exoplayer2.video.HevcConfig;
import com.google.common.collect.ImmutableList;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.NotificationCenter;

/* loaded from: classes.dex */
public class MatroskaExtractor implements Extractor {
    private static final Map TRACK_NAME_TO_ROTATION_DEGREES;
    private int blockAdditionalId;
    private long blockDurationUs;
    private int blockFlags;
    private long blockGroupDiscardPaddingNs;
    private boolean blockHasReferenceBlock;
    private int blockSampleCount;
    private int blockSampleIndex;
    private int[] blockSampleSizes;
    private int blockState;
    private long blockTimeUs;
    private int blockTrackNumber;
    private int blockTrackNumberLength;
    private long clusterTimecodeUs;
    private LongArray cueClusterPositions;
    private LongArray cueTimesUs;
    private long cuesContentPosition;
    private Track currentTrack;
    private long durationTimecode;
    private long durationUs;
    private final ParsableByteArray encryptionInitializationVector;
    private final ParsableByteArray encryptionSubsampleData;
    private ByteBuffer encryptionSubsampleDataBuffer;
    private ExtractorOutput extractorOutput;
    private boolean haveOutputSample;
    private final ParsableByteArray nalLength;
    private final ParsableByteArray nalStartCode;
    private final EbmlReader reader;
    private int sampleBytesRead;
    private int sampleBytesWritten;
    private int sampleCurrentNalBytesRemaining;
    private boolean sampleEncodingHandled;
    private boolean sampleInitializationVectorRead;
    private int samplePartitionCount;
    private boolean samplePartitionCountRead;
    private byte sampleSignalByte;
    private boolean sampleSignalByteRead;
    private final ParsableByteArray sampleStrippedBytes;
    private final ParsableByteArray scratch;
    private int seekEntryId;
    private final ParsableByteArray seekEntryIdBytes;
    private long seekEntryPosition;
    private boolean seekForCues;
    private final boolean seekForCuesEnabled;
    private long seekPositionAfterBuildingCues;
    private boolean seenClusterPositionForCurrentCuePoint;
    private long segmentContentPosition;
    private long segmentContentSize;
    private boolean sentSeekMap;
    private final ParsableByteArray subtitleSample;
    private final ParsableByteArray supplementalData;
    private long timecodeScale;
    private final SparseArray tracks;
    private final VarintReader varintReader;
    private final ParsableByteArray vorbisNumPageSamples;
    public static final ExtractorsFactory FACTORY = new ExtractorsFactory() { // from class: com.google.android.exoplayer2.extractor.mkv.MatroskaExtractor$$ExternalSyntheticLambda0
        @Override // com.google.android.exoplayer2.extractor.ExtractorsFactory
        public final Extractor[] createExtractors() {
            Extractor[] lambda$static$0;
            lambda$static$0 = MatroskaExtractor.lambda$static$0();
            return lambda$static$0;
        }

        @Override // com.google.android.exoplayer2.extractor.ExtractorsFactory
        public /* synthetic */ Extractor[] createExtractors(Uri uri, Map map) {
            Extractor[] createExtractors;
            createExtractors = createExtractors();
            return createExtractors;
        }
    };
    private static final byte[] SUBRIP_PREFIX = {49, 10, 48, 48, 58, 48, 48, 58, 48, 48, 44, 48, 48, 48, 32, 45, 45, 62, 32, 48, 48, 58, 48, 48, 58, 48, 48, 44, 48, 48, 48, 10};
    private static final byte[] SSA_DIALOGUE_FORMAT = Util.getUtf8Bytes("Format: Start, End, ReadOrder, Layer, Style, Name, MarginL, MarginR, MarginV, Effect, Text");
    private static final byte[] SSA_PREFIX = {68, 105, 97, 108, 111, 103, 117, 101, 58, 32, 48, 58, 48, 48, 58, 48, 48, 58, 48, 48, 44, 48, 58, 48, 48, 58, 48, 48, 58, 48, 48, 44};
    private static final byte[] VTT_PREFIX = {87, 69, 66, 86, 84, 84, 10, 10, 48, 48, 58, 48, 48, 58, 48, 48, 46, 48, 48, 48, 32, 45, 45, 62, 32, 48, 48, 58, 48, 48, 58, 48, 48, 46, 48, 48, 48, 10};
    private static final UUID WAVE_SUBFORMAT_PCM = new UUID(72057594037932032L, -9223371306706625679L);

    /* loaded from: classes.dex */
    private final class InnerEbmlProcessor implements EbmlProcessor {
        private InnerEbmlProcessor() {
        }

        @Override // com.google.android.exoplayer2.extractor.mkv.EbmlProcessor
        public void binaryElement(int i, int i2, ExtractorInput extractorInput) {
            MatroskaExtractor.this.binaryElement(i, i2, extractorInput);
        }

        @Override // com.google.android.exoplayer2.extractor.mkv.EbmlProcessor
        public void endMasterElement(int i) {
            MatroskaExtractor.this.endMasterElement(i);
        }

        @Override // com.google.android.exoplayer2.extractor.mkv.EbmlProcessor
        public void floatElement(int i, double d) {
            MatroskaExtractor.this.floatElement(i, d);
        }

        @Override // com.google.android.exoplayer2.extractor.mkv.EbmlProcessor
        public int getElementType(int i) {
            return MatroskaExtractor.this.getElementType(i);
        }

        @Override // com.google.android.exoplayer2.extractor.mkv.EbmlProcessor
        public void integerElement(int i, long j) {
            MatroskaExtractor.this.integerElement(i, j);
        }

        @Override // com.google.android.exoplayer2.extractor.mkv.EbmlProcessor
        public boolean isLevel1Element(int i) {
            return MatroskaExtractor.this.isLevel1Element(i);
        }

        @Override // com.google.android.exoplayer2.extractor.mkv.EbmlProcessor
        public void startMasterElement(int i, long j, long j2) {
            MatroskaExtractor.this.startMasterElement(i, j, j2);
        }

        @Override // com.google.android.exoplayer2.extractor.mkv.EbmlProcessor
        public void stringElement(int i, String str) {
            MatroskaExtractor.this.stringElement(i, str);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public static final class Track {
        private int blockAddIdType;
        public String codecId;
        public byte[] codecPrivate;
        public TrackOutput.CryptoData cryptoData;
        public int defaultSampleDurationNs;
        public byte[] dolbyVisionConfigBytes;
        public DrmInitData drmInitData;
        public boolean flagForced;
        public boolean hasContentEncryption;
        public int maxBlockAdditionId;
        public int nalUnitLengthFieldLength;
        public String name;
        public int number;
        public TrackOutput output;
        public byte[] sampleStrippedBytes;
        public TrueHdSampleRechunker trueHdSampleRechunker;
        public int type;
        public int width = -1;
        public int height = -1;
        public int displayWidth = -1;
        public int displayHeight = -1;
        public int displayUnit = 0;
        public int projectionType = -1;
        public float projectionPoseYaw = 0.0f;
        public float projectionPosePitch = 0.0f;
        public float projectionPoseRoll = 0.0f;
        public byte[] projectionData = null;
        public int stereoMode = -1;
        public boolean hasColorInfo = false;
        public int colorSpace = -1;
        public int colorTransfer = -1;
        public int colorRange = -1;
        public int maxContentLuminance = 1000;
        public int maxFrameAverageLuminance = NotificationCenter.storyQualityUpdate;
        public float primaryRChromaticityX = -1.0f;
        public float primaryRChromaticityY = -1.0f;
        public float primaryGChromaticityX = -1.0f;
        public float primaryGChromaticityY = -1.0f;
        public float primaryBChromaticityX = -1.0f;
        public float primaryBChromaticityY = -1.0f;
        public float whitePointChromaticityX = -1.0f;
        public float whitePointChromaticityY = -1.0f;
        public float maxMasteringLuminance = -1.0f;
        public float minMasteringLuminance = -1.0f;
        public int channelCount = 1;
        public int audioBitDepth = -1;
        public int sampleRate = 8000;
        public long codecDelayNs = 0;
        public long seekPreRollNs = 0;
        public boolean flagDefault = true;
        private String language = "eng";

        protected Track() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void assertOutputInitialized() {
            Assertions.checkNotNull(this.output);
        }

        private byte[] getCodecPrivate(String str) {
            byte[] bArr = this.codecPrivate;
            if (bArr != null) {
                return bArr;
            }
            throw ParserException.createForMalformedContainer("Missing CodecPrivate for codec " + str, null);
        }

        private byte[] getHdrStaticInfo() {
            if (this.primaryRChromaticityX == -1.0f || this.primaryRChromaticityY == -1.0f || this.primaryGChromaticityX == -1.0f || this.primaryGChromaticityY == -1.0f || this.primaryBChromaticityX == -1.0f || this.primaryBChromaticityY == -1.0f || this.whitePointChromaticityX == -1.0f || this.whitePointChromaticityY == -1.0f || this.maxMasteringLuminance == -1.0f || this.minMasteringLuminance == -1.0f) {
                return null;
            }
            byte[] bArr = new byte[25];
            ByteBuffer order = ByteBuffer.wrap(bArr).order(ByteOrder.LITTLE_ENDIAN);
            order.put((byte) 0);
            order.putShort((short) ((this.primaryRChromaticityX * 50000.0f) + 0.5f));
            order.putShort((short) ((this.primaryRChromaticityY * 50000.0f) + 0.5f));
            order.putShort((short) ((this.primaryGChromaticityX * 50000.0f) + 0.5f));
            order.putShort((short) ((this.primaryGChromaticityY * 50000.0f) + 0.5f));
            order.putShort((short) ((this.primaryBChromaticityX * 50000.0f) + 0.5f));
            order.putShort((short) ((this.primaryBChromaticityY * 50000.0f) + 0.5f));
            order.putShort((short) ((this.whitePointChromaticityX * 50000.0f) + 0.5f));
            order.putShort((short) ((this.whitePointChromaticityY * 50000.0f) + 0.5f));
            order.putShort((short) (this.maxMasteringLuminance + 0.5f));
            order.putShort((short) (this.minMasteringLuminance + 0.5f));
            order.putShort((short) this.maxContentLuminance);
            order.putShort((short) this.maxFrameAverageLuminance);
            return bArr;
        }

        private static Pair parseFourCcPrivate(ParsableByteArray parsableByteArray) {
            try {
                parsableByteArray.skipBytes(16);
                long readLittleEndianUnsignedInt = parsableByteArray.readLittleEndianUnsignedInt();
                if (readLittleEndianUnsignedInt == 1482049860) {
                    return new Pair("video/divx", null);
                }
                if (readLittleEndianUnsignedInt == 859189832) {
                    return new Pair("video/3gpp", null);
                }
                if (readLittleEndianUnsignedInt != 826496599) {
                    Log.w("MatroskaExtractor", "Unknown FourCC. Setting mimeType to video/x-unknown");
                    return new Pair("video/x-unknown", null);
                }
                byte[] data = parsableByteArray.getData();
                for (int position = parsableByteArray.getPosition() + 20; position < data.length - 4; position++) {
                    if (data[position] == 0 && data[position + 1] == 0 && data[position + 2] == 1 && data[position + 3] == 15) {
                        return new Pair("video/wvc1", Collections.singletonList(Arrays.copyOfRange(data, position, data.length)));
                    }
                }
                throw ParserException.createForMalformedContainer("Failed to find FourCC VC1 initialization data", null);
            } catch (ArrayIndexOutOfBoundsException unused) {
                throw ParserException.createForMalformedContainer("Error parsing FourCC private data", null);
            }
        }

        private static boolean parseMsAcmCodecPrivate(ParsableByteArray parsableByteArray) {
            try {
                int readLittleEndianUnsignedShort = parsableByteArray.readLittleEndianUnsignedShort();
                if (readLittleEndianUnsignedShort == 1) {
                    return true;
                }
                if (readLittleEndianUnsignedShort != 65534) {
                    return false;
                }
                parsableByteArray.setPosition(24);
                if (parsableByteArray.readLong() == MatroskaExtractor.WAVE_SUBFORMAT_PCM.getMostSignificantBits()) {
                    if (parsableByteArray.readLong() == MatroskaExtractor.WAVE_SUBFORMAT_PCM.getLeastSignificantBits()) {
                        return true;
                    }
                }
                return false;
            } catch (ArrayIndexOutOfBoundsException unused) {
                throw ParserException.createForMalformedContainer("Error parsing MS/ACM codec private", null);
            }
        }

        private static List parseVorbisCodecPrivate(byte[] bArr) {
            int i;
            int i2;
            try {
                if (bArr[0] != 2) {
                    throw ParserException.createForMalformedContainer("Error parsing vorbis codec private", null);
                }
                int i3 = 1;
                int i4 = 0;
                while (true) {
                    i = bArr[i3] & NotificationCenter.notificationsCountUpdated;
                    if (i != 255) {
                        break;
                    }
                    i4 += NotificationCenter.notificationsCountUpdated;
                    i3++;
                }
                int i5 = i3 + 1;
                int i6 = i4 + i;
                int i7 = 0;
                while (true) {
                    i2 = bArr[i5] & NotificationCenter.notificationsCountUpdated;
                    if (i2 != 255) {
                        break;
                    }
                    i7 += NotificationCenter.notificationsCountUpdated;
                    i5++;
                }
                int i8 = i5 + 1;
                int i9 = i7 + i2;
                if (bArr[i8] != 1) {
                    throw ParserException.createForMalformedContainer("Error parsing vorbis codec private", null);
                }
                byte[] bArr2 = new byte[i6];
                System.arraycopy(bArr, i8, bArr2, 0, i6);
                int i10 = i8 + i6;
                if (bArr[i10] != 3) {
                    throw ParserException.createForMalformedContainer("Error parsing vorbis codec private", null);
                }
                int i11 = i10 + i9;
                if (bArr[i11] != 5) {
                    throw ParserException.createForMalformedContainer("Error parsing vorbis codec private", null);
                }
                byte[] bArr3 = new byte[bArr.length - i11];
                System.arraycopy(bArr, i11, bArr3, 0, bArr.length - i11);
                ArrayList arrayList = new ArrayList(2);
                arrayList.add(bArr2);
                arrayList.add(bArr3);
                return arrayList;
            } catch (ArrayIndexOutOfBoundsException unused) {
                throw ParserException.createForMalformedContainer("Error parsing vorbis codec private", null);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean samplesHaveSupplementalData(boolean z) {
            return "A_OPUS".equals(this.codecId) ? z : this.maxBlockAdditionId > 0;
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Failed to find 'out' block for switch in B:5:0x01df. Please report as an issue. */
        /* JADX WARN: Removed duplicated region for block: B:14:0x0409  */
        /* JADX WARN: Removed duplicated region for block: B:19:0x0420  */
        /* JADX WARN: Removed duplicated region for block: B:22:0x042f  */
        /* JADX WARN: Removed duplicated region for block: B:25:0x0551  */
        /* JADX WARN: Removed duplicated region for block: B:30:0x0441  */
        /* JADX WARN: Removed duplicated region for block: B:92:0x0422  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void initializeOutput(ExtractorOutput extractorOutput, int i) {
            char c;
            String str;
            List singletonList;
            String str2;
            StringBuilder sb;
            String str3;
            String str4;
            int i2;
            String str5;
            List list;
            String str6;
            String str7;
            String str8;
            Format.Builder builder;
            int i3;
            int i4;
            int i5;
            DolbyVisionConfig parse;
            String str9 = this.codecId;
            str9.hashCode();
            int i6 = 4;
            int i7 = 0;
            switch (str9.hashCode()) {
                case -2095576542:
                    if (str9.equals("V_MPEG4/ISO/AP")) {
                        c = 0;
                        break;
                    }
                    c = 65535;
                    break;
                case -2095575984:
                    if (str9.equals("V_MPEG4/ISO/SP")) {
                        c = 1;
                        break;
                    }
                    c = 65535;
                    break;
                case -1985379776:
                    if (str9.equals("A_MS/ACM")) {
                        c = 2;
                        break;
                    }
                    c = 65535;
                    break;
                case -1784763192:
                    if (str9.equals("A_TRUEHD")) {
                        c = 3;
                        break;
                    }
                    c = 65535;
                    break;
                case -1730367663:
                    if (str9.equals("A_VORBIS")) {
                        c = 4;
                        break;
                    }
                    c = 65535;
                    break;
                case -1482641358:
                    if (str9.equals("A_MPEG/L2")) {
                        c = 5;
                        break;
                    }
                    c = 65535;
                    break;
                case -1482641357:
                    if (str9.equals("A_MPEG/L3")) {
                        c = 6;
                        break;
                    }
                    c = 65535;
                    break;
                case -1373388978:
                    if (str9.equals("V_MS/VFW/FOURCC")) {
                        c = 7;
                        break;
                    }
                    c = 65535;
                    break;
                case -933872740:
                    if (str9.equals("S_DVBSUB")) {
                        c = '\b';
                        break;
                    }
                    c = 65535;
                    break;
                case -538363189:
                    if (str9.equals("V_MPEG4/ISO/ASP")) {
                        c = '\t';
                        break;
                    }
                    c = 65535;
                    break;
                case -538363109:
                    if (str9.equals("V_MPEG4/ISO/AVC")) {
                        c = '\n';
                        break;
                    }
                    c = 65535;
                    break;
                case -425012669:
                    if (str9.equals("S_VOBSUB")) {
                        c = 11;
                        break;
                    }
                    c = 65535;
                    break;
                case -356037306:
                    if (str9.equals("A_DTS/LOSSLESS")) {
                        c = '\f';
                        break;
                    }
                    c = 65535;
                    break;
                case 62923557:
                    if (str9.equals("A_AAC")) {
                        c = '\r';
                        break;
                    }
                    c = 65535;
                    break;
                case 62923603:
                    if (str9.equals("A_AC3")) {
                        c = 14;
                        break;
                    }
                    c = 65535;
                    break;
                case 62927045:
                    if (str9.equals("A_DTS")) {
                        c = 15;
                        break;
                    }
                    c = 65535;
                    break;
                case 82318131:
                    if (str9.equals("V_AV1")) {
                        c = 16;
                        break;
                    }
                    c = 65535;
                    break;
                case 82338133:
                    if (str9.equals("V_VP8")) {
                        c = 17;
                        break;
                    }
                    c = 65535;
                    break;
                case 82338134:
                    if (str9.equals("V_VP9")) {
                        c = 18;
                        break;
                    }
                    c = 65535;
                    break;
                case 99146302:
                    if (str9.equals("S_HDMV/PGS")) {
                        c = 19;
                        break;
                    }
                    c = 65535;
                    break;
                case 444813526:
                    if (str9.equals("V_THEORA")) {
                        c = 20;
                        break;
                    }
                    c = 65535;
                    break;
                case 542569478:
                    if (str9.equals("A_DTS/EXPRESS")) {
                        c = 21;
                        break;
                    }
                    c = 65535;
                    break;
                case 635596514:
                    if (str9.equals("A_PCM/FLOAT/IEEE")) {
                        c = 22;
                        break;
                    }
                    c = 65535;
                    break;
                case 725948237:
                    if (str9.equals("A_PCM/INT/BIG")) {
                        c = 23;
                        break;
                    }
                    c = 65535;
                    break;
                case 725957860:
                    if (str9.equals("A_PCM/INT/LIT")) {
                        c = 24;
                        break;
                    }
                    c = 65535;
                    break;
                case 738597099:
                    if (str9.equals("S_TEXT/ASS")) {
                        c = 25;
                        break;
                    }
                    c = 65535;
                    break;
                case 855502857:
                    if (str9.equals("V_MPEGH/ISO/HEVC")) {
                        c = 26;
                        break;
                    }
                    c = 65535;
                    break;
                case 1045209816:
                    if (str9.equals("S_TEXT/WEBVTT")) {
                        c = 27;
                        break;
                    }
                    c = 65535;
                    break;
                case 1422270023:
                    if (str9.equals("S_TEXT/UTF8")) {
                        c = 28;
                        break;
                    }
                    c = 65535;
                    break;
                case 1809237540:
                    if (str9.equals("V_MPEG2")) {
                        c = 29;
                        break;
                    }
                    c = 65535;
                    break;
                case 1950749482:
                    if (str9.equals("A_EAC3")) {
                        c = 30;
                        break;
                    }
                    c = 65535;
                    break;
                case 1950789798:
                    if (str9.equals("A_FLAC")) {
                        c = 31;
                        break;
                    }
                    c = 65535;
                    break;
                case 1951062397:
                    if (str9.equals("A_OPUS")) {
                        c = ' ';
                        break;
                    }
                    c = 65535;
                    break;
                default:
                    c = 65535;
                    break;
            }
            String str10 = ". Setting mimeType to ";
            String str11 = "audio/raw";
            switch (c) {
                case 0:
                case 1:
                case '\t':
                    str = "application/dvbsubs";
                    byte[] bArr = this.codecPrivate;
                    singletonList = bArr == null ? null : Collections.singletonList(bArr);
                    str2 = "video/mp4v-es";
                    i6 = -1;
                    str7 = str2;
                    str8 = null;
                    str5 = str8;
                    str4 = str7;
                    i2 = -1;
                    if (this.dolbyVisionConfigBytes != null && (parse = DolbyVisionConfig.parse(new ParsableByteArray(this.dolbyVisionConfigBytes))) != null) {
                        str5 = parse.codecs;
                        str4 = "video/dolby-vision";
                    }
                    int i8 = (this.flagDefault ? 1 : 0) | (!this.flagForced ? 2 : 0);
                    builder = new Format.Builder();
                    if (!MimeTypes.isAudio(str4)) {
                        builder.setChannelCount(this.channelCount).setSampleRate(this.sampleRate).setPcmEncoding(i6);
                        i3 = 1;
                    } else if (MimeTypes.isVideo(str4)) {
                        if (this.displayUnit == 0) {
                            int i9 = this.displayWidth;
                            i4 = -1;
                            if (i9 == -1) {
                                i9 = this.width;
                            }
                            this.displayWidth = i9;
                            int i10 = this.displayHeight;
                            if (i10 == -1) {
                                i10 = this.height;
                            }
                            this.displayHeight = i10;
                        } else {
                            i4 = -1;
                        }
                        float f = (this.displayWidth == i4 || (i5 = this.displayHeight) == i4) ? -1.0f : (this.height * r2) / (this.width * i5);
                        ColorInfo colorInfo = this.hasColorInfo ? new ColorInfo(this.colorSpace, this.colorRange, this.colorTransfer, getHdrStaticInfo()) : null;
                        if (this.name != null && MatroskaExtractor.TRACK_NAME_TO_ROTATION_DEGREES.containsKey(this.name)) {
                            i4 = ((Integer) MatroskaExtractor.TRACK_NAME_TO_ROTATION_DEGREES.get(this.name)).intValue();
                        }
                        if (this.projectionType == 0 && Float.compare(this.projectionPoseYaw, 0.0f) == 0 && Float.compare(this.projectionPosePitch, 0.0f) == 0) {
                            if (Float.compare(this.projectionPoseRoll, 0.0f) != 0) {
                                if (Float.compare(this.projectionPosePitch, 90.0f) == 0) {
                                    i7 = 90;
                                } else if (Float.compare(this.projectionPosePitch, -180.0f) == 0 || Float.compare(this.projectionPosePitch, 180.0f) == 0) {
                                    i7 = NotificationCenter.updateBotMenuButton;
                                } else if (Float.compare(this.projectionPosePitch, -90.0f) == 0) {
                                    i7 = NotificationCenter.attachMenuBotsDidLoad;
                                }
                            }
                            builder.setWidth(this.width).setHeight(this.height).setPixelWidthHeightRatio(f).setRotationDegrees(i7).setProjectionData(this.projectionData).setStereoMode(this.stereoMode).setColorInfo(colorInfo);
                            i3 = 2;
                        }
                        i7 = i4;
                        builder.setWidth(this.width).setHeight(this.height).setPixelWidthHeightRatio(f).setRotationDegrees(i7).setProjectionData(this.projectionData).setStereoMode(this.stereoMode).setColorInfo(colorInfo);
                        i3 = 2;
                    } else {
                        if (!"application/x-subrip".equals(str4) && !"text/x-ssa".equals(str4) && !"text/vtt".equals(str4) && !"application/vobsub".equals(str4) && !"application/pgs".equals(str4) && !str.equals(str4)) {
                            throw ParserException.createForMalformedContainer("Unexpected MIME type.", null);
                        }
                        i3 = 3;
                    }
                    if (this.name != null && !MatroskaExtractor.TRACK_NAME_TO_ROTATION_DEGREES.containsKey(this.name)) {
                        builder.setLabel(this.name);
                    }
                    Format build = builder.setId(i).setSampleMimeType(str4).setMaxInputSize(i2).setLanguage(this.language).setSelectionFlags(i8).setInitializationData(singletonList).setCodecs(str5).setDrmInitData(this.drmInitData).build();
                    TrackOutput track = extractorOutput.track(this.number, i3);
                    this.output = track;
                    track.format(build);
                    return;
                case 2:
                    str = "application/dvbsubs";
                    if (!parseMsAcmCodecPrivate(new ParsableByteArray(getCodecPrivate(this.codecId)))) {
                        sb = new StringBuilder();
                        str10 = "Non-PCM MS/ACM is unsupported. Setting mimeType to ";
                        sb.append(str10);
                        sb.append("audio/x-unknown");
                        Log.w("MatroskaExtractor", sb.toString());
                        str11 = "audio/x-unknown";
                        i6 = -1;
                        singletonList = null;
                        str2 = str11;
                        str7 = str2;
                        str8 = null;
                        str5 = str8;
                        str4 = str7;
                        i2 = -1;
                        if (this.dolbyVisionConfigBytes != null) {
                        }
                        int i82 = (this.flagDefault ? 1 : 0) | (!this.flagForced ? 2 : 0);
                        builder = new Format.Builder();
                        if (!MimeTypes.isAudio(str4)) {
                        }
                        if (this.name != null) {
                        }
                        Format build2 = builder.setId(i).setSampleMimeType(str4).setMaxInputSize(i2).setLanguage(this.language).setSelectionFlags(i82).setInitializationData(singletonList).setCodecs(str5).setDrmInitData(this.drmInitData).build();
                        TrackOutput track2 = extractorOutput.track(this.number, i3);
                        this.output = track2;
                        track2.format(build2);
                        return;
                    }
                    int pcmEncoding = Util.getPcmEncoding(this.audioBitDepth);
                    if (pcmEncoding != 0) {
                        i6 = pcmEncoding;
                        singletonList = null;
                        str2 = str11;
                        str7 = str2;
                        str8 = null;
                        str5 = str8;
                        str4 = str7;
                        i2 = -1;
                        if (this.dolbyVisionConfigBytes != null) {
                        }
                        int i822 = (this.flagDefault ? 1 : 0) | (!this.flagForced ? 2 : 0);
                        builder = new Format.Builder();
                        if (!MimeTypes.isAudio(str4)) {
                        }
                        if (this.name != null) {
                        }
                        Format build22 = builder.setId(i).setSampleMimeType(str4).setMaxInputSize(i2).setLanguage(this.language).setSelectionFlags(i822).setInitializationData(singletonList).setCodecs(str5).setDrmInitData(this.drmInitData).build();
                        TrackOutput track22 = extractorOutput.track(this.number, i3);
                        this.output = track22;
                        track22.format(build22);
                        return;
                    }
                    sb = new StringBuilder();
                    str3 = "Unsupported PCM bit depth: ";
                    sb.append(str3);
                    sb.append(this.audioBitDepth);
                    sb.append(str10);
                    sb.append("audio/x-unknown");
                    Log.w("MatroskaExtractor", sb.toString());
                    str11 = "audio/x-unknown";
                    i6 = -1;
                    singletonList = null;
                    str2 = str11;
                    str7 = str2;
                    str8 = null;
                    str5 = str8;
                    str4 = str7;
                    i2 = -1;
                    if (this.dolbyVisionConfigBytes != null) {
                        str5 = parse.codecs;
                        str4 = "video/dolby-vision";
                        break;
                    }
                    int i8222 = (this.flagDefault ? 1 : 0) | (!this.flagForced ? 2 : 0);
                    builder = new Format.Builder();
                    if (!MimeTypes.isAudio(str4)) {
                    }
                    if (this.name != null) {
                        builder.setLabel(this.name);
                        break;
                    }
                    Format build222 = builder.setId(i).setSampleMimeType(str4).setMaxInputSize(i2).setLanguage(this.language).setSelectionFlags(i8222).setInitializationData(singletonList).setCodecs(str5).setDrmInitData(this.drmInitData).build();
                    TrackOutput track222 = extractorOutput.track(this.number, i3);
                    this.output = track222;
                    track222.format(build222);
                    return;
                case 3:
                    str = "application/dvbsubs";
                    this.trueHdSampleRechunker = new TrueHdSampleRechunker();
                    str11 = "audio/true-hd";
                    i6 = -1;
                    singletonList = null;
                    str2 = str11;
                    str7 = str2;
                    str8 = null;
                    str5 = str8;
                    str4 = str7;
                    i2 = -1;
                    if (this.dolbyVisionConfigBytes != null) {
                    }
                    int i82222 = (this.flagDefault ? 1 : 0) | (!this.flagForced ? 2 : 0);
                    builder = new Format.Builder();
                    if (!MimeTypes.isAudio(str4)) {
                    }
                    if (this.name != null) {
                    }
                    Format build2222 = builder.setId(i).setSampleMimeType(str4).setMaxInputSize(i2).setLanguage(this.language).setSelectionFlags(i82222).setInitializationData(singletonList).setCodecs(str5).setDrmInitData(this.drmInitData).build();
                    TrackOutput track2222 = extractorOutput.track(this.number, i3);
                    this.output = track2222;
                    track2222.format(build2222);
                    return;
                case 4:
                    str = "application/dvbsubs";
                    singletonList = parseVorbisCodecPrivate(getCodecPrivate(this.codecId));
                    str4 = "audio/vorbis";
                    i2 = LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM;
                    str5 = null;
                    i6 = -1;
                    if (this.dolbyVisionConfigBytes != null) {
                    }
                    int i822222 = (this.flagDefault ? 1 : 0) | (!this.flagForced ? 2 : 0);
                    builder = new Format.Builder();
                    if (!MimeTypes.isAudio(str4)) {
                    }
                    if (this.name != null) {
                    }
                    Format build22222 = builder.setId(i).setSampleMimeType(str4).setMaxInputSize(i2).setLanguage(this.language).setSelectionFlags(i822222).setInitializationData(singletonList).setCodecs(str5).setDrmInitData(this.drmInitData).build();
                    TrackOutput track22222 = extractorOutput.track(this.number, i3);
                    this.output = track22222;
                    track22222.format(build22222);
                    return;
                case 5:
                    str = "application/dvbsubs";
                    str4 = "audio/mpeg-L2";
                    singletonList = null;
                    str5 = null;
                    i2 = LiteMode.FLAG_ANIMATED_EMOJI_CHAT_NOT_PREMIUM;
                    i6 = -1;
                    if (this.dolbyVisionConfigBytes != null) {
                    }
                    int i8222222 = (this.flagDefault ? 1 : 0) | (!this.flagForced ? 2 : 0);
                    builder = new Format.Builder();
                    if (!MimeTypes.isAudio(str4)) {
                    }
                    if (this.name != null) {
                    }
                    Format build222222 = builder.setId(i).setSampleMimeType(str4).setMaxInputSize(i2).setLanguage(this.language).setSelectionFlags(i8222222).setInitializationData(singletonList).setCodecs(str5).setDrmInitData(this.drmInitData).build();
                    TrackOutput track222222 = extractorOutput.track(this.number, i3);
                    this.output = track222222;
                    track222222.format(build222222);
                    return;
                case 6:
                    str = "application/dvbsubs";
                    str4 = "audio/mpeg";
                    singletonList = null;
                    str5 = null;
                    i2 = LiteMode.FLAG_ANIMATED_EMOJI_CHAT_NOT_PREMIUM;
                    i6 = -1;
                    if (this.dolbyVisionConfigBytes != null) {
                    }
                    int i82222222 = (this.flagDefault ? 1 : 0) | (!this.flagForced ? 2 : 0);
                    builder = new Format.Builder();
                    if (!MimeTypes.isAudio(str4)) {
                    }
                    if (this.name != null) {
                    }
                    Format build2222222 = builder.setId(i).setSampleMimeType(str4).setMaxInputSize(i2).setLanguage(this.language).setSelectionFlags(i82222222).setInitializationData(singletonList).setCodecs(str5).setDrmInitData(this.drmInitData).build();
                    TrackOutput track2222222 = extractorOutput.track(this.number, i3);
                    this.output = track2222222;
                    track2222222.format(build2222222);
                    return;
                case 7:
                    str = "application/dvbsubs";
                    Pair parseFourCcPrivate = parseFourCcPrivate(new ParsableByteArray(getCodecPrivate(this.codecId)));
                    str2 = (String) parseFourCcPrivate.first;
                    singletonList = (List) parseFourCcPrivate.second;
                    i6 = -1;
                    str7 = str2;
                    str8 = null;
                    str5 = str8;
                    str4 = str7;
                    i2 = -1;
                    if (this.dolbyVisionConfigBytes != null) {
                    }
                    int i822222222 = (this.flagDefault ? 1 : 0) | (!this.flagForced ? 2 : 0);
                    builder = new Format.Builder();
                    if (!MimeTypes.isAudio(str4)) {
                    }
                    if (this.name != null) {
                    }
                    Format build22222222 = builder.setId(i).setSampleMimeType(str4).setMaxInputSize(i2).setLanguage(this.language).setSelectionFlags(i822222222).setInitializationData(singletonList).setCodecs(str5).setDrmInitData(this.drmInitData).build();
                    TrackOutput track22222222 = extractorOutput.track(this.number, i3);
                    this.output = track22222222;
                    track22222222.format(build22222222);
                    return;
                case '\b':
                    str = "application/dvbsubs";
                    byte[] bArr2 = new byte[4];
                    System.arraycopy(getCodecPrivate(this.codecId), 0, bArr2, 0, 4);
                    singletonList = ImmutableList.of((Object) bArr2);
                    str2 = str;
                    i6 = -1;
                    str7 = str2;
                    str8 = null;
                    str5 = str8;
                    str4 = str7;
                    i2 = -1;
                    if (this.dolbyVisionConfigBytes != null) {
                    }
                    int i8222222222 = (this.flagDefault ? 1 : 0) | (!this.flagForced ? 2 : 0);
                    builder = new Format.Builder();
                    if (!MimeTypes.isAudio(str4)) {
                    }
                    if (this.name != null) {
                    }
                    Format build222222222 = builder.setId(i).setSampleMimeType(str4).setMaxInputSize(i2).setLanguage(this.language).setSelectionFlags(i8222222222).setInitializationData(singletonList).setCodecs(str5).setDrmInitData(this.drmInitData).build();
                    TrackOutput track222222222 = extractorOutput.track(this.number, i3);
                    this.output = track222222222;
                    track222222222.format(build222222222);
                    return;
                case '\n':
                    str = "application/dvbsubs";
                    AvcConfig parse2 = AvcConfig.parse(new ParsableByteArray(getCodecPrivate(this.codecId)));
                    list = parse2.initializationData;
                    this.nalUnitLengthFieldLength = parse2.nalUnitLengthFieldLength;
                    str6 = parse2.codecs;
                    str7 = MediaController.VIDEO_MIME_TYPE;
                    i6 = -1;
                    List list2 = list;
                    str8 = str6;
                    singletonList = list2;
                    str5 = str8;
                    str4 = str7;
                    i2 = -1;
                    if (this.dolbyVisionConfigBytes != null) {
                    }
                    int i82222222222 = (this.flagDefault ? 1 : 0) | (!this.flagForced ? 2 : 0);
                    builder = new Format.Builder();
                    if (!MimeTypes.isAudio(str4)) {
                    }
                    if (this.name != null) {
                    }
                    Format build2222222222 = builder.setId(i).setSampleMimeType(str4).setMaxInputSize(i2).setLanguage(this.language).setSelectionFlags(i82222222222).setInitializationData(singletonList).setCodecs(str5).setDrmInitData(this.drmInitData).build();
                    TrackOutput track2222222222 = extractorOutput.track(this.number, i3);
                    this.output = track2222222222;
                    track2222222222.format(build2222222222);
                    return;
                case 11:
                    str = "application/dvbsubs";
                    singletonList = ImmutableList.of((Object) getCodecPrivate(this.codecId));
                    str2 = "application/vobsub";
                    i6 = -1;
                    str7 = str2;
                    str8 = null;
                    str5 = str8;
                    str4 = str7;
                    i2 = -1;
                    if (this.dolbyVisionConfigBytes != null) {
                    }
                    int i822222222222 = (this.flagDefault ? 1 : 0) | (!this.flagForced ? 2 : 0);
                    builder = new Format.Builder();
                    if (!MimeTypes.isAudio(str4)) {
                    }
                    if (this.name != null) {
                    }
                    Format build22222222222 = builder.setId(i).setSampleMimeType(str4).setMaxInputSize(i2).setLanguage(this.language).setSelectionFlags(i822222222222).setInitializationData(singletonList).setCodecs(str5).setDrmInitData(this.drmInitData).build();
                    TrackOutput track22222222222 = extractorOutput.track(this.number, i3);
                    this.output = track22222222222;
                    track22222222222.format(build22222222222);
                    return;
                case '\f':
                    str = "application/dvbsubs";
                    str11 = "audio/vnd.dts.hd";
                    i6 = -1;
                    singletonList = null;
                    str2 = str11;
                    str7 = str2;
                    str8 = null;
                    str5 = str8;
                    str4 = str7;
                    i2 = -1;
                    if (this.dolbyVisionConfigBytes != null) {
                    }
                    int i8222222222222 = (this.flagDefault ? 1 : 0) | (!this.flagForced ? 2 : 0);
                    builder = new Format.Builder();
                    if (!MimeTypes.isAudio(str4)) {
                    }
                    if (this.name != null) {
                    }
                    Format build222222222222 = builder.setId(i).setSampleMimeType(str4).setMaxInputSize(i2).setLanguage(this.language).setSelectionFlags(i8222222222222).setInitializationData(singletonList).setCodecs(str5).setDrmInitData(this.drmInitData).build();
                    TrackOutput track222222222222 = extractorOutput.track(this.number, i3);
                    this.output = track222222222222;
                    track222222222222.format(build222222222222);
                    return;
                case '\r':
                    str = "application/dvbsubs";
                    singletonList = Collections.singletonList(getCodecPrivate(this.codecId));
                    AacUtil.Config parseAudioSpecificConfig = AacUtil.parseAudioSpecificConfig(this.codecPrivate);
                    this.sampleRate = parseAudioSpecificConfig.sampleRateHz;
                    this.channelCount = parseAudioSpecificConfig.channelCount;
                    str8 = parseAudioSpecificConfig.codecs;
                    str7 = MediaController.AUDIO_MIME_TYPE;
                    i6 = -1;
                    str5 = str8;
                    str4 = str7;
                    i2 = -1;
                    if (this.dolbyVisionConfigBytes != null) {
                    }
                    int i82222222222222 = (this.flagDefault ? 1 : 0) | (!this.flagForced ? 2 : 0);
                    builder = new Format.Builder();
                    if (!MimeTypes.isAudio(str4)) {
                    }
                    if (this.name != null) {
                    }
                    Format build2222222222222 = builder.setId(i).setSampleMimeType(str4).setMaxInputSize(i2).setLanguage(this.language).setSelectionFlags(i82222222222222).setInitializationData(singletonList).setCodecs(str5).setDrmInitData(this.drmInitData).build();
                    TrackOutput track2222222222222 = extractorOutput.track(this.number, i3);
                    this.output = track2222222222222;
                    track2222222222222.format(build2222222222222);
                    return;
                case 14:
                    str = "application/dvbsubs";
                    str11 = "audio/ac3";
                    i6 = -1;
                    singletonList = null;
                    str2 = str11;
                    str7 = str2;
                    str8 = null;
                    str5 = str8;
                    str4 = str7;
                    i2 = -1;
                    if (this.dolbyVisionConfigBytes != null) {
                    }
                    int i822222222222222 = (this.flagDefault ? 1 : 0) | (!this.flagForced ? 2 : 0);
                    builder = new Format.Builder();
                    if (!MimeTypes.isAudio(str4)) {
                    }
                    if (this.name != null) {
                    }
                    Format build22222222222222 = builder.setId(i).setSampleMimeType(str4).setMaxInputSize(i2).setLanguage(this.language).setSelectionFlags(i822222222222222).setInitializationData(singletonList).setCodecs(str5).setDrmInitData(this.drmInitData).build();
                    TrackOutput track22222222222222 = extractorOutput.track(this.number, i3);
                    this.output = track22222222222222;
                    track22222222222222.format(build22222222222222);
                    return;
                case 15:
                case 21:
                    str = "application/dvbsubs";
                    str11 = "audio/vnd.dts";
                    i6 = -1;
                    singletonList = null;
                    str2 = str11;
                    str7 = str2;
                    str8 = null;
                    str5 = str8;
                    str4 = str7;
                    i2 = -1;
                    if (this.dolbyVisionConfigBytes != null) {
                    }
                    int i8222222222222222 = (this.flagDefault ? 1 : 0) | (!this.flagForced ? 2 : 0);
                    builder = new Format.Builder();
                    if (!MimeTypes.isAudio(str4)) {
                    }
                    if (this.name != null) {
                    }
                    Format build222222222222222 = builder.setId(i).setSampleMimeType(str4).setMaxInputSize(i2).setLanguage(this.language).setSelectionFlags(i8222222222222222).setInitializationData(singletonList).setCodecs(str5).setDrmInitData(this.drmInitData).build();
                    TrackOutput track222222222222222 = extractorOutput.track(this.number, i3);
                    this.output = track222222222222222;
                    track222222222222222.format(build222222222222222);
                    return;
                case 16:
                    str = "application/dvbsubs";
                    str11 = "video/av01";
                    i6 = -1;
                    singletonList = null;
                    str2 = str11;
                    str7 = str2;
                    str8 = null;
                    str5 = str8;
                    str4 = str7;
                    i2 = -1;
                    if (this.dolbyVisionConfigBytes != null) {
                    }
                    int i82222222222222222 = (this.flagDefault ? 1 : 0) | (!this.flagForced ? 2 : 0);
                    builder = new Format.Builder();
                    if (!MimeTypes.isAudio(str4)) {
                    }
                    if (this.name != null) {
                    }
                    Format build2222222222222222 = builder.setId(i).setSampleMimeType(str4).setMaxInputSize(i2).setLanguage(this.language).setSelectionFlags(i82222222222222222).setInitializationData(singletonList).setCodecs(str5).setDrmInitData(this.drmInitData).build();
                    TrackOutput track2222222222222222 = extractorOutput.track(this.number, i3);
                    this.output = track2222222222222222;
                    track2222222222222222.format(build2222222222222222);
                    return;
                case 17:
                    str = "application/dvbsubs";
                    str11 = "video/x-vnd.on2.vp8";
                    i6 = -1;
                    singletonList = null;
                    str2 = str11;
                    str7 = str2;
                    str8 = null;
                    str5 = str8;
                    str4 = str7;
                    i2 = -1;
                    if (this.dolbyVisionConfigBytes != null) {
                    }
                    int i822222222222222222 = (this.flagDefault ? 1 : 0) | (!this.flagForced ? 2 : 0);
                    builder = new Format.Builder();
                    if (!MimeTypes.isAudio(str4)) {
                    }
                    if (this.name != null) {
                    }
                    Format build22222222222222222 = builder.setId(i).setSampleMimeType(str4).setMaxInputSize(i2).setLanguage(this.language).setSelectionFlags(i822222222222222222).setInitializationData(singletonList).setCodecs(str5).setDrmInitData(this.drmInitData).build();
                    TrackOutput track22222222222222222 = extractorOutput.track(this.number, i3);
                    this.output = track22222222222222222;
                    track22222222222222222.format(build22222222222222222);
                    return;
                case 18:
                    str = "application/dvbsubs";
                    str11 = "video/x-vnd.on2.vp9";
                    i6 = -1;
                    singletonList = null;
                    str2 = str11;
                    str7 = str2;
                    str8 = null;
                    str5 = str8;
                    str4 = str7;
                    i2 = -1;
                    if (this.dolbyVisionConfigBytes != null) {
                    }
                    int i8222222222222222222 = (this.flagDefault ? 1 : 0) | (!this.flagForced ? 2 : 0);
                    builder = new Format.Builder();
                    if (!MimeTypes.isAudio(str4)) {
                    }
                    if (this.name != null) {
                    }
                    Format build222222222222222222 = builder.setId(i).setSampleMimeType(str4).setMaxInputSize(i2).setLanguage(this.language).setSelectionFlags(i8222222222222222222).setInitializationData(singletonList).setCodecs(str5).setDrmInitData(this.drmInitData).build();
                    TrackOutput track222222222222222222 = extractorOutput.track(this.number, i3);
                    this.output = track222222222222222222;
                    track222222222222222222.format(build222222222222222222);
                    return;
                case 19:
                    str = "application/dvbsubs";
                    str11 = "application/pgs";
                    i6 = -1;
                    singletonList = null;
                    str2 = str11;
                    str7 = str2;
                    str8 = null;
                    str5 = str8;
                    str4 = str7;
                    i2 = -1;
                    if (this.dolbyVisionConfigBytes != null) {
                    }
                    int i82222222222222222222 = (this.flagDefault ? 1 : 0) | (!this.flagForced ? 2 : 0);
                    builder = new Format.Builder();
                    if (!MimeTypes.isAudio(str4)) {
                    }
                    if (this.name != null) {
                    }
                    Format build2222222222222222222 = builder.setId(i).setSampleMimeType(str4).setMaxInputSize(i2).setLanguage(this.language).setSelectionFlags(i82222222222222222222).setInitializationData(singletonList).setCodecs(str5).setDrmInitData(this.drmInitData).build();
                    TrackOutput track2222222222222222222 = extractorOutput.track(this.number, i3);
                    this.output = track2222222222222222222;
                    track2222222222222222222.format(build2222222222222222222);
                    return;
                case 20:
                    str = "application/dvbsubs";
                    str11 = "video/x-unknown";
                    i6 = -1;
                    singletonList = null;
                    str2 = str11;
                    str7 = str2;
                    str8 = null;
                    str5 = str8;
                    str4 = str7;
                    i2 = -1;
                    if (this.dolbyVisionConfigBytes != null) {
                    }
                    int i822222222222222222222 = (this.flagDefault ? 1 : 0) | (!this.flagForced ? 2 : 0);
                    builder = new Format.Builder();
                    if (!MimeTypes.isAudio(str4)) {
                    }
                    if (this.name != null) {
                    }
                    Format build22222222222222222222 = builder.setId(i).setSampleMimeType(str4).setMaxInputSize(i2).setLanguage(this.language).setSelectionFlags(i822222222222222222222).setInitializationData(singletonList).setCodecs(str5).setDrmInitData(this.drmInitData).build();
                    TrackOutput track22222222222222222222 = extractorOutput.track(this.number, i3);
                    this.output = track22222222222222222222;
                    track22222222222222222222.format(build22222222222222222222);
                    return;
                case 22:
                    str = "application/dvbsubs";
                    if (this.audioBitDepth != 32) {
                        sb = new StringBuilder();
                        str3 = "Unsupported floating point PCM bit depth: ";
                        sb.append(str3);
                        sb.append(this.audioBitDepth);
                        sb.append(str10);
                        sb.append("audio/x-unknown");
                        Log.w("MatroskaExtractor", sb.toString());
                        str11 = "audio/x-unknown";
                        i6 = -1;
                    }
                    singletonList = null;
                    str2 = str11;
                    str7 = str2;
                    str8 = null;
                    str5 = str8;
                    str4 = str7;
                    i2 = -1;
                    if (this.dolbyVisionConfigBytes != null) {
                    }
                    int i8222222222222222222222 = (this.flagDefault ? 1 : 0) | (!this.flagForced ? 2 : 0);
                    builder = new Format.Builder();
                    if (!MimeTypes.isAudio(str4)) {
                    }
                    if (this.name != null) {
                    }
                    Format build222222222222222222222 = builder.setId(i).setSampleMimeType(str4).setMaxInputSize(i2).setLanguage(this.language).setSelectionFlags(i8222222222222222222222).setInitializationData(singletonList).setCodecs(str5).setDrmInitData(this.drmInitData).build();
                    TrackOutput track222222222222222222222 = extractorOutput.track(this.number, i3);
                    this.output = track222222222222222222222;
                    track222222222222222222222.format(build222222222222222222222);
                    return;
                case 23:
                    str = "application/dvbsubs";
                    int i11 = this.audioBitDepth;
                    if (i11 == 8) {
                        i6 = 3;
                    } else if (i11 == 16) {
                        i6 = 268435456;
                    } else {
                        sb = new StringBuilder();
                        str3 = "Unsupported big endian PCM bit depth: ";
                        sb.append(str3);
                        sb.append(this.audioBitDepth);
                        sb.append(str10);
                        sb.append("audio/x-unknown");
                        Log.w("MatroskaExtractor", sb.toString());
                        str11 = "audio/x-unknown";
                        i6 = -1;
                    }
                    singletonList = null;
                    str2 = str11;
                    str7 = str2;
                    str8 = null;
                    str5 = str8;
                    str4 = str7;
                    i2 = -1;
                    if (this.dolbyVisionConfigBytes != null) {
                    }
                    int i82222222222222222222222 = (this.flagDefault ? 1 : 0) | (!this.flagForced ? 2 : 0);
                    builder = new Format.Builder();
                    if (!MimeTypes.isAudio(str4)) {
                    }
                    if (this.name != null) {
                    }
                    Format build2222222222222222222222 = builder.setId(i).setSampleMimeType(str4).setMaxInputSize(i2).setLanguage(this.language).setSelectionFlags(i82222222222222222222222).setInitializationData(singletonList).setCodecs(str5).setDrmInitData(this.drmInitData).build();
                    TrackOutput track2222222222222222222222 = extractorOutput.track(this.number, i3);
                    this.output = track2222222222222222222222;
                    track2222222222222222222222.format(build2222222222222222222222);
                    return;
                case 24:
                    str = "application/dvbsubs";
                    i6 = Util.getPcmEncoding(this.audioBitDepth);
                    if (i6 == 0) {
                        sb = new StringBuilder();
                        str3 = "Unsupported little endian PCM bit depth: ";
                        sb.append(str3);
                        sb.append(this.audioBitDepth);
                        sb.append(str10);
                        sb.append("audio/x-unknown");
                        Log.w("MatroskaExtractor", sb.toString());
                        str11 = "audio/x-unknown";
                        i6 = -1;
                    }
                    singletonList = null;
                    str2 = str11;
                    str7 = str2;
                    str8 = null;
                    str5 = str8;
                    str4 = str7;
                    i2 = -1;
                    if (this.dolbyVisionConfigBytes != null) {
                    }
                    int i822222222222222222222222 = (this.flagDefault ? 1 : 0) | (!this.flagForced ? 2 : 0);
                    builder = new Format.Builder();
                    if (!MimeTypes.isAudio(str4)) {
                    }
                    if (this.name != null) {
                    }
                    Format build22222222222222222222222 = builder.setId(i).setSampleMimeType(str4).setMaxInputSize(i2).setLanguage(this.language).setSelectionFlags(i822222222222222222222222).setInitializationData(singletonList).setCodecs(str5).setDrmInitData(this.drmInitData).build();
                    TrackOutput track22222222222222222222222 = extractorOutput.track(this.number, i3);
                    this.output = track22222222222222222222222;
                    track22222222222222222222222.format(build22222222222222222222222);
                    return;
                case 25:
                    str = "application/dvbsubs";
                    singletonList = ImmutableList.of((Object) MatroskaExtractor.SSA_DIALOGUE_FORMAT, (Object) getCodecPrivate(this.codecId));
                    str2 = "text/x-ssa";
                    i6 = -1;
                    str7 = str2;
                    str8 = null;
                    str5 = str8;
                    str4 = str7;
                    i2 = -1;
                    if (this.dolbyVisionConfigBytes != null) {
                    }
                    int i8222222222222222222222222 = (this.flagDefault ? 1 : 0) | (!this.flagForced ? 2 : 0);
                    builder = new Format.Builder();
                    if (!MimeTypes.isAudio(str4)) {
                    }
                    if (this.name != null) {
                    }
                    Format build222222222222222222222222 = builder.setId(i).setSampleMimeType(str4).setMaxInputSize(i2).setLanguage(this.language).setSelectionFlags(i8222222222222222222222222).setInitializationData(singletonList).setCodecs(str5).setDrmInitData(this.drmInitData).build();
                    TrackOutput track222222222222222222222222 = extractorOutput.track(this.number, i3);
                    this.output = track222222222222222222222222;
                    track222222222222222222222222.format(build222222222222222222222222);
                    return;
                case 26:
                    str = "application/dvbsubs";
                    HevcConfig parse3 = HevcConfig.parse(new ParsableByteArray(getCodecPrivate(this.codecId)));
                    list = parse3.initializationData;
                    this.nalUnitLengthFieldLength = parse3.nalUnitLengthFieldLength;
                    str6 = parse3.codecs;
                    str7 = "video/hevc";
                    i6 = -1;
                    List list22 = list;
                    str8 = str6;
                    singletonList = list22;
                    str5 = str8;
                    str4 = str7;
                    i2 = -1;
                    if (this.dolbyVisionConfigBytes != null) {
                    }
                    int i82222222222222222222222222 = (this.flagDefault ? 1 : 0) | (!this.flagForced ? 2 : 0);
                    builder = new Format.Builder();
                    if (!MimeTypes.isAudio(str4)) {
                    }
                    if (this.name != null) {
                    }
                    Format build2222222222222222222222222 = builder.setId(i).setSampleMimeType(str4).setMaxInputSize(i2).setLanguage(this.language).setSelectionFlags(i82222222222222222222222222).setInitializationData(singletonList).setCodecs(str5).setDrmInitData(this.drmInitData).build();
                    TrackOutput track2222222222222222222222222 = extractorOutput.track(this.number, i3);
                    this.output = track2222222222222222222222222;
                    track2222222222222222222222222.format(build2222222222222222222222222);
                    return;
                case 27:
                    str = "application/dvbsubs";
                    str11 = "text/vtt";
                    i6 = -1;
                    singletonList = null;
                    str2 = str11;
                    str7 = str2;
                    str8 = null;
                    str5 = str8;
                    str4 = str7;
                    i2 = -1;
                    if (this.dolbyVisionConfigBytes != null) {
                    }
                    int i822222222222222222222222222 = (this.flagDefault ? 1 : 0) | (!this.flagForced ? 2 : 0);
                    builder = new Format.Builder();
                    if (!MimeTypes.isAudio(str4)) {
                    }
                    if (this.name != null) {
                    }
                    Format build22222222222222222222222222 = builder.setId(i).setSampleMimeType(str4).setMaxInputSize(i2).setLanguage(this.language).setSelectionFlags(i822222222222222222222222222).setInitializationData(singletonList).setCodecs(str5).setDrmInitData(this.drmInitData).build();
                    TrackOutput track22222222222222222222222222 = extractorOutput.track(this.number, i3);
                    this.output = track22222222222222222222222222;
                    track22222222222222222222222222.format(build22222222222222222222222222);
                    return;
                case 28:
                    str = "application/dvbsubs";
                    str11 = "application/x-subrip";
                    i6 = -1;
                    singletonList = null;
                    str2 = str11;
                    str7 = str2;
                    str8 = null;
                    str5 = str8;
                    str4 = str7;
                    i2 = -1;
                    if (this.dolbyVisionConfigBytes != null) {
                    }
                    int i8222222222222222222222222222 = (this.flagDefault ? 1 : 0) | (!this.flagForced ? 2 : 0);
                    builder = new Format.Builder();
                    if (!MimeTypes.isAudio(str4)) {
                    }
                    if (this.name != null) {
                    }
                    Format build222222222222222222222222222 = builder.setId(i).setSampleMimeType(str4).setMaxInputSize(i2).setLanguage(this.language).setSelectionFlags(i8222222222222222222222222222).setInitializationData(singletonList).setCodecs(str5).setDrmInitData(this.drmInitData).build();
                    TrackOutput track222222222222222222222222222 = extractorOutput.track(this.number, i3);
                    this.output = track222222222222222222222222222;
                    track222222222222222222222222222.format(build222222222222222222222222222);
                    return;
                case 29:
                    str = "application/dvbsubs";
                    str11 = "video/mpeg2";
                    i6 = -1;
                    singletonList = null;
                    str2 = str11;
                    str7 = str2;
                    str8 = null;
                    str5 = str8;
                    str4 = str7;
                    i2 = -1;
                    if (this.dolbyVisionConfigBytes != null) {
                    }
                    int i82222222222222222222222222222 = (this.flagDefault ? 1 : 0) | (!this.flagForced ? 2 : 0);
                    builder = new Format.Builder();
                    if (!MimeTypes.isAudio(str4)) {
                    }
                    if (this.name != null) {
                    }
                    Format build2222222222222222222222222222 = builder.setId(i).setSampleMimeType(str4).setMaxInputSize(i2).setLanguage(this.language).setSelectionFlags(i82222222222222222222222222222).setInitializationData(singletonList).setCodecs(str5).setDrmInitData(this.drmInitData).build();
                    TrackOutput track2222222222222222222222222222 = extractorOutput.track(this.number, i3);
                    this.output = track2222222222222222222222222222;
                    track2222222222222222222222222222.format(build2222222222222222222222222222);
                    return;
                case 30:
                    str = "application/dvbsubs";
                    str11 = "audio/eac3";
                    i6 = -1;
                    singletonList = null;
                    str2 = str11;
                    str7 = str2;
                    str8 = null;
                    str5 = str8;
                    str4 = str7;
                    i2 = -1;
                    if (this.dolbyVisionConfigBytes != null) {
                    }
                    int i822222222222222222222222222222 = (this.flagDefault ? 1 : 0) | (!this.flagForced ? 2 : 0);
                    builder = new Format.Builder();
                    if (!MimeTypes.isAudio(str4)) {
                    }
                    if (this.name != null) {
                    }
                    Format build22222222222222222222222222222 = builder.setId(i).setSampleMimeType(str4).setMaxInputSize(i2).setLanguage(this.language).setSelectionFlags(i822222222222222222222222222222).setInitializationData(singletonList).setCodecs(str5).setDrmInitData(this.drmInitData).build();
                    TrackOutput track22222222222222222222222222222 = extractorOutput.track(this.number, i3);
                    this.output = track22222222222222222222222222222;
                    track22222222222222222222222222222.format(build22222222222222222222222222222);
                    return;
                case 31:
                    str = "application/dvbsubs";
                    singletonList = Collections.singletonList(getCodecPrivate(this.codecId));
                    str2 = "audio/flac";
                    i6 = -1;
                    str7 = str2;
                    str8 = null;
                    str5 = str8;
                    str4 = str7;
                    i2 = -1;
                    if (this.dolbyVisionConfigBytes != null) {
                    }
                    int i8222222222222222222222222222222 = (this.flagDefault ? 1 : 0) | (!this.flagForced ? 2 : 0);
                    builder = new Format.Builder();
                    if (!MimeTypes.isAudio(str4)) {
                    }
                    if (this.name != null) {
                    }
                    Format build222222222222222222222222222222 = builder.setId(i).setSampleMimeType(str4).setMaxInputSize(i2).setLanguage(this.language).setSelectionFlags(i8222222222222222222222222222222).setInitializationData(singletonList).setCodecs(str5).setDrmInitData(this.drmInitData).build();
                    TrackOutput track222222222222222222222222222222 = extractorOutput.track(this.number, i3);
                    this.output = track222222222222222222222222222222;
                    track222222222222222222222222222222.format(build222222222222222222222222222222);
                    return;
                case ' ':
                    singletonList = new ArrayList(3);
                    singletonList.add(getCodecPrivate(this.codecId));
                    ByteBuffer allocate = ByteBuffer.allocate(8);
                    ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
                    str = "application/dvbsubs";
                    singletonList.add(allocate.order(byteOrder).putLong(this.codecDelayNs).array());
                    singletonList.add(ByteBuffer.allocate(8).order(byteOrder).putLong(this.seekPreRollNs).array());
                    str4 = "audio/opus";
                    i2 = 5760;
                    str5 = null;
                    i6 = -1;
                    if (this.dolbyVisionConfigBytes != null) {
                    }
                    int i82222222222222222222222222222222 = (this.flagDefault ? 1 : 0) | (!this.flagForced ? 2 : 0);
                    builder = new Format.Builder();
                    if (!MimeTypes.isAudio(str4)) {
                    }
                    if (this.name != null) {
                    }
                    Format build2222222222222222222222222222222 = builder.setId(i).setSampleMimeType(str4).setMaxInputSize(i2).setLanguage(this.language).setSelectionFlags(i82222222222222222222222222222222).setInitializationData(singletonList).setCodecs(str5).setDrmInitData(this.drmInitData).build();
                    TrackOutput track2222222222222222222222222222222 = extractorOutput.track(this.number, i3);
                    this.output = track2222222222222222222222222222222;
                    track2222222222222222222222222222222.format(build2222222222222222222222222222222);
                    return;
                default:
                    throw ParserException.createForMalformedContainer("Unrecognized codec identifier.", null);
            }
        }

        public void outputPendingSampleMetadata() {
            TrueHdSampleRechunker trueHdSampleRechunker = this.trueHdSampleRechunker;
            if (trueHdSampleRechunker != null) {
                trueHdSampleRechunker.outputPendingSampleMetadata(this.output, this.cryptoData);
            }
        }

        public void reset() {
            TrueHdSampleRechunker trueHdSampleRechunker = this.trueHdSampleRechunker;
            if (trueHdSampleRechunker != null) {
                trueHdSampleRechunker.reset();
            }
        }
    }

    static {
        HashMap hashMap = new HashMap();
        hashMap.put("htc_video_rotA-000", 0);
        hashMap.put("htc_video_rotA-090", 90);
        hashMap.put("htc_video_rotA-180", Integer.valueOf(NotificationCenter.updateBotMenuButton));
        hashMap.put("htc_video_rotA-270", Integer.valueOf(NotificationCenter.attachMenuBotsDidLoad));
        TRACK_NAME_TO_ROTATION_DEGREES = Collections.unmodifiableMap(hashMap);
    }

    public MatroskaExtractor() {
        this(0);
    }

    public MatroskaExtractor(int i) {
        this(new DefaultEbmlReader(), i);
    }

    MatroskaExtractor(EbmlReader ebmlReader, int i) {
        this.segmentContentPosition = -1L;
        this.timecodeScale = -9223372036854775807L;
        this.durationTimecode = -9223372036854775807L;
        this.durationUs = -9223372036854775807L;
        this.cuesContentPosition = -1L;
        this.seekPositionAfterBuildingCues = -1L;
        this.clusterTimecodeUs = -9223372036854775807L;
        this.reader = ebmlReader;
        ebmlReader.init(new InnerEbmlProcessor());
        this.seekForCuesEnabled = (i & 1) == 0;
        this.varintReader = new VarintReader();
        this.tracks = new SparseArray();
        this.scratch = new ParsableByteArray(4);
        this.vorbisNumPageSamples = new ParsableByteArray(ByteBuffer.allocate(4).putInt(-1).array());
        this.seekEntryIdBytes = new ParsableByteArray(4);
        this.nalStartCode = new ParsableByteArray(NalUnitUtil.NAL_START_CODE);
        this.nalLength = new ParsableByteArray(4);
        this.sampleStrippedBytes = new ParsableByteArray();
        this.subtitleSample = new ParsableByteArray();
        this.encryptionInitializationVector = new ParsableByteArray(8);
        this.encryptionSubsampleData = new ParsableByteArray();
        this.supplementalData = new ParsableByteArray();
        this.blockSampleSizes = new int[1];
    }

    private void assertInCues(int i) {
        if (this.cueTimesUs == null || this.cueClusterPositions == null) {
            throw ParserException.createForMalformedContainer("Element " + i + " must be in a Cues", null);
        }
    }

    private void assertInTrackEntry(int i) {
        if (this.currentTrack != null) {
            return;
        }
        throw ParserException.createForMalformedContainer("Element " + i + " must be in a TrackEntry", null);
    }

    private void assertInitialized() {
        Assertions.checkStateNotNull(this.extractorOutput);
    }

    private SeekMap buildSeekMap(LongArray longArray, LongArray longArray2) {
        int i;
        if (this.segmentContentPosition == -1 || this.durationUs == -9223372036854775807L || longArray == null || longArray.size() == 0 || longArray2 == null || longArray2.size() != longArray.size()) {
            return new SeekMap.Unseekable(this.durationUs);
        }
        int size = longArray.size();
        int[] iArr = new int[size];
        long[] jArr = new long[size];
        long[] jArr2 = new long[size];
        long[] jArr3 = new long[size];
        int i2 = 0;
        for (int i3 = 0; i3 < size; i3++) {
            jArr3[i3] = longArray.get(i3);
            jArr[i3] = this.segmentContentPosition + longArray2.get(i3);
        }
        while (true) {
            i = size - 1;
            if (i2 >= i) {
                break;
            }
            int i4 = i2 + 1;
            iArr[i2] = (int) (jArr[i4] - jArr[i2]);
            jArr2[i2] = jArr3[i4] - jArr3[i2];
            i2 = i4;
        }
        iArr[i] = (int) ((this.segmentContentPosition + this.segmentContentSize) - jArr[i]);
        long j = this.durationUs - jArr3[i];
        jArr2[i] = j;
        if (j <= 0) {
            Log.w("MatroskaExtractor", "Discarding last cue point with unexpected duration: " + j);
            iArr = Arrays.copyOf(iArr, i);
            jArr = Arrays.copyOf(jArr, i);
            jArr2 = Arrays.copyOf(jArr2, i);
            jArr3 = Arrays.copyOf(jArr3, i);
        }
        return new ChunkIndex(iArr, jArr, jArr2, jArr3);
    }

    private void commitSampleToOutput(Track track, long j, int i, int i2, int i3) {
        String str;
        TrueHdSampleRechunker trueHdSampleRechunker = track.trueHdSampleRechunker;
        if (trueHdSampleRechunker != null) {
            trueHdSampleRechunker.sampleMetadata(track.output, j, i, i2, i3, track.cryptoData);
        } else {
            if ("S_TEXT/UTF8".equals(track.codecId) || "S_TEXT/ASS".equals(track.codecId) || "S_TEXT/WEBVTT".equals(track.codecId)) {
                if (this.blockSampleCount > 1) {
                    str = "Skipping subtitle sample in laced block.";
                } else {
                    long j2 = this.blockDurationUs;
                    if (j2 == -9223372036854775807L) {
                        str = "Skipping subtitle sample with no duration.";
                    } else {
                        setSubtitleEndTime(track.codecId, j2, this.subtitleSample.getData());
                        int position = this.subtitleSample.getPosition();
                        while (true) {
                            if (position >= this.subtitleSample.limit()) {
                                break;
                            }
                            if (this.subtitleSample.getData()[position] == 0) {
                                this.subtitleSample.setLimit(position);
                                break;
                            }
                            position++;
                        }
                        TrackOutput trackOutput = track.output;
                        ParsableByteArray parsableByteArray = this.subtitleSample;
                        trackOutput.sampleData(parsableByteArray, parsableByteArray.limit());
                        i2 += this.subtitleSample.limit();
                    }
                }
                Log.w("MatroskaExtractor", str);
            }
            if ((268435456 & i) != 0) {
                if (this.blockSampleCount > 1) {
                    this.supplementalData.reset(0);
                } else {
                    int limit = this.supplementalData.limit();
                    track.output.sampleData(this.supplementalData, limit, 2);
                    i2 += limit;
                }
            }
            track.output.sampleMetadata(j, i, i2, i3, track.cryptoData);
        }
        this.haveOutputSample = true;
    }

    private static int[] ensureArrayCapacity(int[] iArr, int i) {
        return iArr == null ? new int[i] : iArr.length >= i ? iArr : new int[Math.max(iArr.length * 2, i)];
    }

    private int finishWriteSampleData() {
        int i = this.sampleBytesWritten;
        resetWriteSampleData();
        return i;
    }

    private static byte[] formatSubtitleTimecode(long j, String str, long j2) {
        Assertions.checkArgument(j != -9223372036854775807L);
        int i = (int) (j / 3600000000L);
        long j3 = j - (i * 3600000000L);
        int i2 = (int) (j3 / 60000000);
        long j4 = j3 - (i2 * 60000000);
        int i3 = (int) (j4 / 1000000);
        return Util.getUtf8Bytes(String.format(Locale.US, str, Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf((int) ((j4 - (i3 * 1000000)) / j2))));
    }

    private static boolean isCodecSupported(String str) {
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -2095576542:
                if (str.equals("V_MPEG4/ISO/AP")) {
                    c = 0;
                    break;
                }
                break;
            case -2095575984:
                if (str.equals("V_MPEG4/ISO/SP")) {
                    c = 1;
                    break;
                }
                break;
            case -1985379776:
                if (str.equals("A_MS/ACM")) {
                    c = 2;
                    break;
                }
                break;
            case -1784763192:
                if (str.equals("A_TRUEHD")) {
                    c = 3;
                    break;
                }
                break;
            case -1730367663:
                if (str.equals("A_VORBIS")) {
                    c = 4;
                    break;
                }
                break;
            case -1482641358:
                if (str.equals("A_MPEG/L2")) {
                    c = 5;
                    break;
                }
                break;
            case -1482641357:
                if (str.equals("A_MPEG/L3")) {
                    c = 6;
                    break;
                }
                break;
            case -1373388978:
                if (str.equals("V_MS/VFW/FOURCC")) {
                    c = 7;
                    break;
                }
                break;
            case -933872740:
                if (str.equals("S_DVBSUB")) {
                    c = '\b';
                    break;
                }
                break;
            case -538363189:
                if (str.equals("V_MPEG4/ISO/ASP")) {
                    c = '\t';
                    break;
                }
                break;
            case -538363109:
                if (str.equals("V_MPEG4/ISO/AVC")) {
                    c = '\n';
                    break;
                }
                break;
            case -425012669:
                if (str.equals("S_VOBSUB")) {
                    c = 11;
                    break;
                }
                break;
            case -356037306:
                if (str.equals("A_DTS/LOSSLESS")) {
                    c = '\f';
                    break;
                }
                break;
            case 62923557:
                if (str.equals("A_AAC")) {
                    c = '\r';
                    break;
                }
                break;
            case 62923603:
                if (str.equals("A_AC3")) {
                    c = 14;
                    break;
                }
                break;
            case 62927045:
                if (str.equals("A_DTS")) {
                    c = 15;
                    break;
                }
                break;
            case 82318131:
                if (str.equals("V_AV1")) {
                    c = 16;
                    break;
                }
                break;
            case 82338133:
                if (str.equals("V_VP8")) {
                    c = 17;
                    break;
                }
                break;
            case 82338134:
                if (str.equals("V_VP9")) {
                    c = 18;
                    break;
                }
                break;
            case 99146302:
                if (str.equals("S_HDMV/PGS")) {
                    c = 19;
                    break;
                }
                break;
            case 444813526:
                if (str.equals("V_THEORA")) {
                    c = 20;
                    break;
                }
                break;
            case 542569478:
                if (str.equals("A_DTS/EXPRESS")) {
                    c = 21;
                    break;
                }
                break;
            case 635596514:
                if (str.equals("A_PCM/FLOAT/IEEE")) {
                    c = 22;
                    break;
                }
                break;
            case 725948237:
                if (str.equals("A_PCM/INT/BIG")) {
                    c = 23;
                    break;
                }
                break;
            case 725957860:
                if (str.equals("A_PCM/INT/LIT")) {
                    c = 24;
                    break;
                }
                break;
            case 738597099:
                if (str.equals("S_TEXT/ASS")) {
                    c = 25;
                    break;
                }
                break;
            case 855502857:
                if (str.equals("V_MPEGH/ISO/HEVC")) {
                    c = 26;
                    break;
                }
                break;
            case 1045209816:
                if (str.equals("S_TEXT/WEBVTT")) {
                    c = 27;
                    break;
                }
                break;
            case 1422270023:
                if (str.equals("S_TEXT/UTF8")) {
                    c = 28;
                    break;
                }
                break;
            case 1809237540:
                if (str.equals("V_MPEG2")) {
                    c = 29;
                    break;
                }
                break;
            case 1950749482:
                if (str.equals("A_EAC3")) {
                    c = 30;
                    break;
                }
                break;
            case 1950789798:
                if (str.equals("A_FLAC")) {
                    c = 31;
                    break;
                }
                break;
            case 1951062397:
                if (str.equals("A_OPUS")) {
                    c = ' ';
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case '\b':
            case '\t':
            case '\n':
            case 11:
            case '\f':
            case '\r':
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case ' ':
                return true;
            default:
                return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Extractor[] lambda$static$0() {
        return new Extractor[]{new MatroskaExtractor()};
    }

    private boolean maybeSeekForCues(PositionHolder positionHolder, long j) {
        if (this.seekForCues) {
            this.seekPositionAfterBuildingCues = j;
            positionHolder.position = this.cuesContentPosition;
            this.seekForCues = false;
            return true;
        }
        if (this.sentSeekMap) {
            long j2 = this.seekPositionAfterBuildingCues;
            if (j2 != -1) {
                positionHolder.position = j2;
                this.seekPositionAfterBuildingCues = -1L;
                return true;
            }
        }
        return false;
    }

    private void readScratch(ExtractorInput extractorInput, int i) {
        if (this.scratch.limit() >= i) {
            return;
        }
        if (this.scratch.capacity() < i) {
            ParsableByteArray parsableByteArray = this.scratch;
            parsableByteArray.ensureCapacity(Math.max(parsableByteArray.capacity() * 2, i));
        }
        extractorInput.readFully(this.scratch.getData(), this.scratch.limit(), i - this.scratch.limit());
        this.scratch.setLimit(i);
    }

    private void resetWriteSampleData() {
        this.sampleBytesRead = 0;
        this.sampleBytesWritten = 0;
        this.sampleCurrentNalBytesRemaining = 0;
        this.sampleEncodingHandled = false;
        this.sampleSignalByteRead = false;
        this.samplePartitionCountRead = false;
        this.samplePartitionCount = 0;
        this.sampleSignalByte = (byte) 0;
        this.sampleInitializationVectorRead = false;
        this.sampleStrippedBytes.reset(0);
    }

    private long scaleTimecodeToUs(long j) {
        long j2 = this.timecodeScale;
        if (j2 != -9223372036854775807L) {
            return Util.scaleLargeTimestamp(j, j2, 1000L);
        }
        throw ParserException.createForMalformedContainer("Can't scale timecode prior to timecodeScale being set.", null);
    }

    private static void setSubtitleEndTime(String str, long j, byte[] bArr) {
        byte[] formatSubtitleTimecode;
        int i;
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case 738597099:
                if (str.equals("S_TEXT/ASS")) {
                    c = 0;
                    break;
                }
                break;
            case 1045209816:
                if (str.equals("S_TEXT/WEBVTT")) {
                    c = 1;
                    break;
                }
                break;
            case 1422270023:
                if (str.equals("S_TEXT/UTF8")) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                formatSubtitleTimecode = formatSubtitleTimecode(j, "%01d:%02d:%02d:%02d", 10000L);
                i = 21;
                break;
            case 1:
                formatSubtitleTimecode = formatSubtitleTimecode(j, "%02d:%02d:%02d.%03d", 1000L);
                i = 25;
                break;
            case 2:
                formatSubtitleTimecode = formatSubtitleTimecode(j, "%02d:%02d:%02d,%03d", 1000L);
                i = 19;
                break;
            default:
                throw new IllegalArgumentException();
        }
        System.arraycopy(formatSubtitleTimecode, 0, bArr, i, formatSubtitleTimecode.length);
    }

    private int writeSampleData(ExtractorInput extractorInput, Track track, int i, boolean z) {
        int i2;
        if ("S_TEXT/UTF8".equals(track.codecId)) {
            writeSubtitleSampleData(extractorInput, SUBRIP_PREFIX, i);
        } else if ("S_TEXT/ASS".equals(track.codecId)) {
            writeSubtitleSampleData(extractorInput, SSA_PREFIX, i);
        } else {
            if (!"S_TEXT/WEBVTT".equals(track.codecId)) {
                TrackOutput trackOutput = track.output;
                if (!this.sampleEncodingHandled) {
                    if (track.hasContentEncryption) {
                        this.blockFlags &= -1073741825;
                        if (!this.sampleSignalByteRead) {
                            extractorInput.readFully(this.scratch.getData(), 0, 1);
                            this.sampleBytesRead++;
                            if ((this.scratch.getData()[0] & 128) == 128) {
                                throw ParserException.createForMalformedContainer("Extension bit is set in signal byte", null);
                            }
                            this.sampleSignalByte = this.scratch.getData()[0];
                            this.sampleSignalByteRead = true;
                        }
                        byte b = this.sampleSignalByte;
                        if ((b & 1) == 1) {
                            boolean z2 = (b & 2) == 2;
                            this.blockFlags |= 1073741824;
                            if (!this.sampleInitializationVectorRead) {
                                extractorInput.readFully(this.encryptionInitializationVector.getData(), 0, 8);
                                this.sampleBytesRead += 8;
                                this.sampleInitializationVectorRead = true;
                                this.scratch.getData()[0] = (byte) ((z2 ? 128 : 0) | 8);
                                this.scratch.setPosition(0);
                                trackOutput.sampleData(this.scratch, 1, 1);
                                this.sampleBytesWritten++;
                                this.encryptionInitializationVector.setPosition(0);
                                trackOutput.sampleData(this.encryptionInitializationVector, 8, 1);
                                this.sampleBytesWritten += 8;
                            }
                            if (z2) {
                                if (!this.samplePartitionCountRead) {
                                    extractorInput.readFully(this.scratch.getData(), 0, 1);
                                    this.sampleBytesRead++;
                                    this.scratch.setPosition(0);
                                    this.samplePartitionCount = this.scratch.readUnsignedByte();
                                    this.samplePartitionCountRead = true;
                                }
                                int i3 = this.samplePartitionCount * 4;
                                this.scratch.reset(i3);
                                extractorInput.readFully(this.scratch.getData(), 0, i3);
                                this.sampleBytesRead += i3;
                                short s = (short) ((this.samplePartitionCount / 2) + 1);
                                int i4 = (s * 6) + 2;
                                ByteBuffer byteBuffer = this.encryptionSubsampleDataBuffer;
                                if (byteBuffer == null || byteBuffer.capacity() < i4) {
                                    this.encryptionSubsampleDataBuffer = ByteBuffer.allocate(i4);
                                }
                                this.encryptionSubsampleDataBuffer.position(0);
                                this.encryptionSubsampleDataBuffer.putShort(s);
                                int i5 = 0;
                                int i6 = 0;
                                while (true) {
                                    i2 = this.samplePartitionCount;
                                    if (i5 >= i2) {
                                        break;
                                    }
                                    int readUnsignedIntToInt = this.scratch.readUnsignedIntToInt();
                                    if (i5 % 2 == 0) {
                                        this.encryptionSubsampleDataBuffer.putShort((short) (readUnsignedIntToInt - i6));
                                    } else {
                                        this.encryptionSubsampleDataBuffer.putInt(readUnsignedIntToInt - i6);
                                    }
                                    i5++;
                                    i6 = readUnsignedIntToInt;
                                }
                                int i7 = (i - this.sampleBytesRead) - i6;
                                int i8 = i2 % 2;
                                ByteBuffer byteBuffer2 = this.encryptionSubsampleDataBuffer;
                                if (i8 == 1) {
                                    byteBuffer2.putInt(i7);
                                } else {
                                    byteBuffer2.putShort((short) i7);
                                    this.encryptionSubsampleDataBuffer.putInt(0);
                                }
                                this.encryptionSubsampleData.reset(this.encryptionSubsampleDataBuffer.array(), i4);
                                trackOutput.sampleData(this.encryptionSubsampleData, i4, 1);
                                this.sampleBytesWritten += i4;
                            }
                        }
                    } else {
                        byte[] bArr = track.sampleStrippedBytes;
                        if (bArr != null) {
                            this.sampleStrippedBytes.reset(bArr, bArr.length);
                        }
                    }
                    if (track.samplesHaveSupplementalData(z)) {
                        this.blockFlags |= 268435456;
                        this.supplementalData.reset(0);
                        int limit = (this.sampleStrippedBytes.limit() + i) - this.sampleBytesRead;
                        this.scratch.reset(4);
                        this.scratch.getData()[0] = (byte) ((limit >> 24) & NotificationCenter.notificationsCountUpdated);
                        this.scratch.getData()[1] = (byte) ((limit >> 16) & NotificationCenter.notificationsCountUpdated);
                        this.scratch.getData()[2] = (byte) ((limit >> 8) & NotificationCenter.notificationsCountUpdated);
                        this.scratch.getData()[3] = (byte) (limit & NotificationCenter.notificationsCountUpdated);
                        trackOutput.sampleData(this.scratch, 4, 2);
                        this.sampleBytesWritten += 4;
                    }
                    this.sampleEncodingHandled = true;
                }
                int limit2 = i + this.sampleStrippedBytes.limit();
                if (!"V_MPEG4/ISO/AVC".equals(track.codecId) && !"V_MPEGH/ISO/HEVC".equals(track.codecId)) {
                    if (track.trueHdSampleRechunker != null) {
                        Assertions.checkState(this.sampleStrippedBytes.limit() == 0);
                        track.trueHdSampleRechunker.startSample(extractorInput);
                    }
                    while (true) {
                        int i9 = this.sampleBytesRead;
                        if (i9 >= limit2) {
                            break;
                        }
                        int writeToOutput = writeToOutput(extractorInput, trackOutput, limit2 - i9);
                        this.sampleBytesRead += writeToOutput;
                        this.sampleBytesWritten += writeToOutput;
                    }
                } else {
                    byte[] data = this.nalLength.getData();
                    data[0] = 0;
                    data[1] = 0;
                    data[2] = 0;
                    int i10 = track.nalUnitLengthFieldLength;
                    int i11 = 4 - i10;
                    while (this.sampleBytesRead < limit2) {
                        int i12 = this.sampleCurrentNalBytesRemaining;
                        if (i12 == 0) {
                            writeToTarget(extractorInput, data, i11, i10);
                            this.sampleBytesRead += i10;
                            this.nalLength.setPosition(0);
                            this.sampleCurrentNalBytesRemaining = this.nalLength.readUnsignedIntToInt();
                            this.nalStartCode.setPosition(0);
                            trackOutput.sampleData(this.nalStartCode, 4);
                            this.sampleBytesWritten += 4;
                        } else {
                            int writeToOutput2 = writeToOutput(extractorInput, trackOutput, i12);
                            this.sampleBytesRead += writeToOutput2;
                            this.sampleBytesWritten += writeToOutput2;
                            this.sampleCurrentNalBytesRemaining -= writeToOutput2;
                        }
                    }
                }
                if ("A_VORBIS".equals(track.codecId)) {
                    this.vorbisNumPageSamples.setPosition(0);
                    trackOutput.sampleData(this.vorbisNumPageSamples, 4);
                    this.sampleBytesWritten += 4;
                }
                return finishWriteSampleData();
            }
            writeSubtitleSampleData(extractorInput, VTT_PREFIX, i);
        }
        return finishWriteSampleData();
    }

    private void writeSubtitleSampleData(ExtractorInput extractorInput, byte[] bArr, int i) {
        int length = bArr.length + i;
        if (this.subtitleSample.capacity() < length) {
            this.subtitleSample.reset(Arrays.copyOf(bArr, length + i));
        } else {
            System.arraycopy(bArr, 0, this.subtitleSample.getData(), 0, bArr.length);
        }
        extractorInput.readFully(this.subtitleSample.getData(), bArr.length, i);
        this.subtitleSample.setPosition(0);
        this.subtitleSample.setLimit(length);
    }

    private int writeToOutput(ExtractorInput extractorInput, TrackOutput trackOutput, int i) {
        int bytesLeft = this.sampleStrippedBytes.bytesLeft();
        if (bytesLeft <= 0) {
            return trackOutput.sampleData((DataReader) extractorInput, i, false);
        }
        int min = Math.min(i, bytesLeft);
        trackOutput.sampleData(this.sampleStrippedBytes, min);
        return min;
    }

    private void writeToTarget(ExtractorInput extractorInput, byte[] bArr, int i, int i2) {
        int min = Math.min(i2, this.sampleStrippedBytes.bytesLeft());
        extractorInput.readFully(bArr, i + min, i2 - min);
        if (min > 0) {
            this.sampleStrippedBytes.readBytes(bArr, i, min);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:119:0x0229, code lost:
    
        throw com.google.android.exoplayer2.ParserException.createForMalformedContainer("EBML lacing sample size out of range.", null);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void binaryElement(int i, int i2, ExtractorInput extractorInput) {
        Track track;
        Track track2;
        Track track3;
        long j;
        int i3;
        int i4;
        int i5;
        int i6 = 1;
        int i7 = 0;
        if (i != 161 && i != 163) {
            if (i == 165) {
                if (this.blockState != 2) {
                    return;
                }
                handleBlockAdditionalData((Track) this.tracks.get(this.blockTrackNumber), this.blockAdditionalId, extractorInput, i2);
                return;
            }
            if (i == 16877) {
                handleBlockAddIDExtraData(getCurrentTrack(i), extractorInput, i2);
                return;
            }
            if (i == 16981) {
                assertInTrackEntry(i);
                byte[] bArr = new byte[i2];
                this.currentTrack.sampleStrippedBytes = bArr;
                extractorInput.readFully(bArr, 0, i2);
                return;
            }
            if (i == 18402) {
                byte[] bArr2 = new byte[i2];
                extractorInput.readFully(bArr2, 0, i2);
                getCurrentTrack(i).cryptoData = new TrackOutput.CryptoData(1, bArr2, 0, 0);
                return;
            }
            if (i == 21419) {
                Arrays.fill(this.seekEntryIdBytes.getData(), (byte) 0);
                extractorInput.readFully(this.seekEntryIdBytes.getData(), 4 - i2, i2);
                this.seekEntryIdBytes.setPosition(0);
                this.seekEntryId = (int) this.seekEntryIdBytes.readUnsignedInt();
                return;
            }
            if (i == 25506) {
                assertInTrackEntry(i);
                byte[] bArr3 = new byte[i2];
                this.currentTrack.codecPrivate = bArr3;
                extractorInput.readFully(bArr3, 0, i2);
                return;
            }
            if (i != 30322) {
                throw ParserException.createForMalformedContainer("Unexpected id: " + i, null);
            }
            assertInTrackEntry(i);
            byte[] bArr4 = new byte[i2];
            this.currentTrack.projectionData = bArr4;
            extractorInput.readFully(bArr4, 0, i2);
            return;
        }
        if (this.blockState == 0) {
            this.blockTrackNumber = (int) this.varintReader.readUnsignedVarint(extractorInput, false, true, 8);
            this.blockTrackNumberLength = this.varintReader.getLastLength();
            this.blockDurationUs = -9223372036854775807L;
            this.blockState = 1;
            this.scratch.reset(0);
        }
        Track track4 = (Track) this.tracks.get(this.blockTrackNumber);
        if (track4 == null) {
            extractorInput.skipFully(i2 - this.blockTrackNumberLength);
            this.blockState = 0;
            return;
        }
        track4.assertOutputInitialized();
        if (this.blockState == 1) {
            readScratch(extractorInput, 3);
            int i8 = (this.scratch.getData()[2] & 6) >> 1;
            if (i8 == 0) {
                this.blockSampleCount = 1;
                int[] ensureArrayCapacity = ensureArrayCapacity(this.blockSampleSizes, 1);
                this.blockSampleSizes = ensureArrayCapacity;
                ensureArrayCapacity[0] = (i2 - this.blockTrackNumberLength) - 3;
            } else {
                int i9 = 4;
                readScratch(extractorInput, 4);
                int i10 = (this.scratch.getData()[3] & 255) + 1;
                this.blockSampleCount = i10;
                int[] ensureArrayCapacity2 = ensureArrayCapacity(this.blockSampleSizes, i10);
                this.blockSampleSizes = ensureArrayCapacity2;
                if (i8 == 2) {
                    int i11 = (i2 - this.blockTrackNumberLength) - 4;
                    int i12 = this.blockSampleCount;
                    Arrays.fill(ensureArrayCapacity2, 0, i12, i11 / i12);
                } else if (i8 == 1) {
                    int i13 = 0;
                    int i14 = 0;
                    while (true) {
                        i3 = this.blockSampleCount - 1;
                        if (i13 >= i3) {
                            break;
                        }
                        this.blockSampleSizes[i13] = 0;
                        while (true) {
                            i4 = i9 + 1;
                            readScratch(extractorInput, i4);
                            int i15 = this.scratch.getData()[i9] & 255;
                            int[] iArr = this.blockSampleSizes;
                            i5 = iArr[i13] + i15;
                            iArr[i13] = i5;
                            if (i15 != 255) {
                                break;
                            } else {
                                i9 = i4;
                            }
                        }
                        i14 += i5;
                        i13++;
                        i9 = i4;
                    }
                    this.blockSampleSizes[i3] = ((i2 - this.blockTrackNumberLength) - i9) - i14;
                } else {
                    if (i8 != 3) {
                        throw ParserException.createForMalformedContainer("Unexpected lacing value: " + i8, null);
                    }
                    int i16 = 0;
                    int i17 = 0;
                    while (true) {
                        int i18 = this.blockSampleCount - i6;
                        if (i16 >= i18) {
                            track2 = track4;
                            this.blockSampleSizes[i18] = ((i2 - this.blockTrackNumberLength) - i9) - i17;
                            break;
                        }
                        this.blockSampleSizes[i16] = i7;
                        int i19 = i9 + 1;
                        readScratch(extractorInput, i19);
                        if (this.scratch.getData()[i9] == 0) {
                            throw ParserException.createForMalformedContainer("No valid varint length mask found", null);
                        }
                        int i20 = 0;
                        while (true) {
                            if (i20 >= 8) {
                                track3 = track4;
                                j = 0;
                                break;
                            }
                            int i21 = i6 << (7 - i20);
                            if ((this.scratch.getData()[i9] & i21) != 0) {
                                int i22 = i19 + i20;
                                readScratch(extractorInput, i22);
                                track3 = track4;
                                j = this.scratch.getData()[i9] & 255 & (i21 ^ (-1));
                                while (i19 < i22) {
                                    j = (j << 8) | (this.scratch.getData()[i19] & 255);
                                    i19++;
                                }
                                if (i16 > 0) {
                                    j -= (1 << ((i20 * 7) + 6)) - 1;
                                    i9 = i22;
                                } else {
                                    i19 = i22;
                                }
                            } else {
                                i20++;
                                i6 = 1;
                            }
                        }
                        i9 = i19;
                        if (j < -2147483648L || j > 2147483647L) {
                            break;
                        }
                        int i23 = (int) j;
                        int[] iArr2 = this.blockSampleSizes;
                        if (i16 != 0) {
                            i23 += iArr2[i16 - 1];
                        }
                        iArr2[i16] = i23;
                        i17 += i23;
                        i16++;
                        track4 = track3;
                        i6 = 1;
                        i7 = 0;
                    }
                }
            }
            track2 = track4;
            this.blockTimeUs = this.clusterTimecodeUs + scaleTimecodeToUs((this.scratch.getData()[0] << 8) | (this.scratch.getData()[1] & 255));
            track = track2;
            this.blockFlags = (track.type == 2 || (i == 163 && (this.scratch.getData()[2] & 128) == 128)) ? 1 : 0;
            this.blockState = 2;
            this.blockSampleIndex = 0;
        } else {
            track = track4;
        }
        if (i == 163) {
            while (true) {
                int i24 = this.blockSampleIndex;
                if (i24 >= this.blockSampleCount) {
                    this.blockState = 0;
                    return;
                } else {
                    commitSampleToOutput(track, ((this.blockSampleIndex * track.defaultSampleDurationNs) / 1000) + this.blockTimeUs, this.blockFlags, writeSampleData(extractorInput, track, this.blockSampleSizes[i24], false), 0);
                    this.blockSampleIndex++;
                }
            }
        } else {
            while (true) {
                int i25 = this.blockSampleIndex;
                if (i25 >= this.blockSampleCount) {
                    return;
                }
                int[] iArr3 = this.blockSampleSizes;
                iArr3[i25] = writeSampleData(extractorInput, track, iArr3[i25], true);
                this.blockSampleIndex++;
            }
        }
    }

    protected void endMasterElement(int i) {
        assertInitialized();
        if (i == 160) {
            if (this.blockState != 2) {
                return;
            }
            Track track = (Track) this.tracks.get(this.blockTrackNumber);
            track.assertOutputInitialized();
            if (this.blockGroupDiscardPaddingNs > 0 && "A_OPUS".equals(track.codecId)) {
                this.supplementalData.reset(ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(this.blockGroupDiscardPaddingNs).array());
            }
            int i2 = 0;
            for (int i3 = 0; i3 < this.blockSampleCount; i3++) {
                i2 += this.blockSampleSizes[i3];
            }
            int i4 = 0;
            while (i4 < this.blockSampleCount) {
                long j = this.blockTimeUs + ((track.defaultSampleDurationNs * i4) / 1000);
                int i5 = this.blockFlags;
                if (i4 == 0 && !this.blockHasReferenceBlock) {
                    i5 |= 1;
                }
                int i6 = this.blockSampleSizes[i4];
                int i7 = i2 - i6;
                commitSampleToOutput(track, j, i5, i6, i7);
                i4++;
                i2 = i7;
            }
            this.blockState = 0;
            return;
        }
        if (i == 174) {
            Track track2 = (Track) Assertions.checkStateNotNull(this.currentTrack);
            String str = track2.codecId;
            if (str == null) {
                throw ParserException.createForMalformedContainer("CodecId is missing in TrackEntry element", null);
            }
            if (isCodecSupported(str)) {
                track2.initializeOutput(this.extractorOutput, track2.number);
                this.tracks.put(track2.number, track2);
            }
            this.currentTrack = null;
            return;
        }
        if (i == 19899) {
            int i8 = this.seekEntryId;
            if (i8 != -1) {
                long j2 = this.seekEntryPosition;
                if (j2 != -1) {
                    if (i8 == 475249515) {
                        this.cuesContentPosition = j2;
                        return;
                    }
                    return;
                }
            }
            throw ParserException.createForMalformedContainer("Mandatory element SeekID or SeekPosition not found", null);
        }
        if (i == 25152) {
            assertInTrackEntry(i);
            Track track3 = this.currentTrack;
            if (track3.hasContentEncryption) {
                if (track3.cryptoData == null) {
                    throw ParserException.createForMalformedContainer("Encrypted Track found but ContentEncKeyID was not found", null);
                }
                track3.drmInitData = new DrmInitData(new DrmInitData.SchemeData(C.UUID_NIL, "video/webm", this.currentTrack.cryptoData.encryptionKey));
                return;
            }
            return;
        }
        if (i == 28032) {
            assertInTrackEntry(i);
            Track track4 = this.currentTrack;
            if (track4.hasContentEncryption && track4.sampleStrippedBytes != null) {
                throw ParserException.createForMalformedContainer("Combining encryption and compression is not supported", null);
            }
            return;
        }
        if (i == 357149030) {
            if (this.timecodeScale == -9223372036854775807L) {
                this.timecodeScale = 1000000L;
            }
            long j3 = this.durationTimecode;
            if (j3 != -9223372036854775807L) {
                this.durationUs = scaleTimecodeToUs(j3);
                return;
            }
            return;
        }
        if (i == 374648427) {
            if (this.tracks.size() == 0) {
                throw ParserException.createForMalformedContainer("No valid tracks were found", null);
            }
            this.extractorOutput.endTracks();
        } else {
            if (i != 475249515) {
                return;
            }
            if (!this.sentSeekMap) {
                this.extractorOutput.seekMap(buildSeekMap(this.cueTimesUs, this.cueClusterPositions));
                this.sentSeekMap = true;
            }
            this.cueTimesUs = null;
            this.cueClusterPositions = null;
        }
    }

    protected void floatElement(int i, double d) {
        if (i == 181) {
            getCurrentTrack(i).sampleRate = (int) d;
            return;
        }
        if (i == 17545) {
            this.durationTimecode = (long) d;
            return;
        }
        switch (i) {
            case 21969:
                getCurrentTrack(i).primaryRChromaticityX = (float) d;
                return;
            case 21970:
                getCurrentTrack(i).primaryRChromaticityY = (float) d;
                return;
            case 21971:
                getCurrentTrack(i).primaryGChromaticityX = (float) d;
                return;
            case 21972:
                getCurrentTrack(i).primaryGChromaticityY = (float) d;
                return;
            case 21973:
                getCurrentTrack(i).primaryBChromaticityX = (float) d;
                return;
            case 21974:
                getCurrentTrack(i).primaryBChromaticityY = (float) d;
                return;
            case 21975:
                getCurrentTrack(i).whitePointChromaticityX = (float) d;
                return;
            case 21976:
                getCurrentTrack(i).whitePointChromaticityY = (float) d;
                return;
            case 21977:
                getCurrentTrack(i).maxMasteringLuminance = (float) d;
                return;
            case 21978:
                getCurrentTrack(i).minMasteringLuminance = (float) d;
                return;
            default:
                switch (i) {
                    case 30323:
                        getCurrentTrack(i).projectionPoseYaw = (float) d;
                        return;
                    case 30324:
                        getCurrentTrack(i).projectionPosePitch = (float) d;
                        return;
                    case 30325:
                        getCurrentTrack(i).projectionPoseRoll = (float) d;
                        return;
                    default:
                        return;
                }
        }
    }

    protected Track getCurrentTrack(int i) {
        assertInTrackEntry(i);
        return this.currentTrack;
    }

    protected int getElementType(int i) {
        switch (i) {
            case NotificationCenter.httpFileDidLoad /* 131 */:
            case NotificationCenter.fileUploadProgressChanged /* 136 */:
            case NotificationCenter.recordResumed /* 155 */:
            case 159:
            case NotificationCenter.themeUploadError /* 176 */:
            case NotificationCenter.suggestedFiltersLoaded /* 179 */:
            case NotificationCenter.didUpdatePremiumGiftFieldIcon /* 186 */:
            case NotificationCenter.factCheckLoaded /* 215 */:
            case NotificationCenter.emojiLoaded /* 231 */:
            case NotificationCenter.didApplyNewTheme /* 238 */:
            case NotificationCenter.needShareTheme /* 241 */:
            case NotificationCenter.proxyChangedByRotation /* 251 */:
            case 16871:
            case 16980:
            case 17029:
            case 17143:
            case 18401:
            case 18408:
            case 20529:
            case 20530:
            case 21420:
            case 21432:
            case 21680:
            case 21682:
            case 21690:
            case 21930:
            case 21945:
            case 21946:
            case 21947:
            case 21948:
            case 21949:
            case 21998:
            case 22186:
            case 22203:
            case 25188:
            case 30114:
            case 30321:
            case 2352003:
            case 2807729:
                return 2;
            case NotificationCenter.fileUploaded /* 134 */:
            case 17026:
            case 21358:
            case 2274716:
                return 3;
            case NotificationCenter.audioRouteChanged /* 160 */:
            case NotificationCenter.applyGroupCallVisibleParticipants /* 166 */:
            case NotificationCenter.newEmojiSuggestionsAvailable /* 174 */:
            case NotificationCenter.boostedChannelByUser /* 183 */:
            case NotificationCenter.storiesEnabledUpdate /* 187 */:
            case NotificationCenter.updateStories /* 224 */:
            case NotificationCenter.botDownloadsUpdate /* 225 */:
            case 16868:
            case 18407:
            case 19899:
            case 20532:
            case 20533:
            case 21936:
            case 21968:
            case 25152:
            case 28032:
            case 30113:
            case 30320:
            case 290298740:
            case 357149030:
            case 374648427:
            case 408125543:
            case 440786851:
            case 475249515:
            case 524531317:
                return 1;
            case NotificationCenter.didStartedCall /* 161 */:
            case NotificationCenter.groupCallSpeakingUsersUpdated /* 163 */:
            case NotificationCenter.activeGroupCallsUpdated /* 165 */:
            case 16877:
            case 16981:
            case 18402:
            case 21419:
            case 25506:
            case 30322:
                return 4;
            case NotificationCenter.giftsToUserSent /* 181 */:
            case 17545:
            case 21969:
            case 21970:
            case 21971:
            case 21972:
            case 21973:
            case 21974:
            case 21975:
            case 21976:
            case 21977:
            case 21978:
            case 30323:
            case 30324:
            case 30325:
                return 5;
            default:
                return 0;
        }
    }

    protected void handleBlockAddIDExtraData(Track track, ExtractorInput extractorInput, int i) {
        if (track.blockAddIdType != 1685485123 && track.blockAddIdType != 1685480259) {
            extractorInput.skipFully(i);
            return;
        }
        byte[] bArr = new byte[i];
        track.dolbyVisionConfigBytes = bArr;
        extractorInput.readFully(bArr, 0, i);
    }

    protected void handleBlockAdditionalData(Track track, int i, ExtractorInput extractorInput, int i2) {
        if (i != 4 || !"V_VP9".equals(track.codecId)) {
            extractorInput.skipFully(i2);
        } else {
            this.supplementalData.reset(i2);
            extractorInput.readFully(this.supplementalData.getData(), 0, i2);
        }
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public final void init(ExtractorOutput extractorOutput) {
        this.extractorOutput = extractorOutput;
    }

    protected void integerElement(int i, long j) {
        if (i == 20529) {
            if (j == 0) {
                return;
            }
            throw ParserException.createForMalformedContainer("ContentEncodingOrder " + j + " not supported", null);
        }
        if (i == 20530) {
            if (j == 1) {
                return;
            }
            throw ParserException.createForMalformedContainer("ContentEncodingScope " + j + " not supported", null);
        }
        switch (i) {
            case NotificationCenter.httpFileDidLoad /* 131 */:
                getCurrentTrack(i).type = (int) j;
                return;
            case NotificationCenter.fileUploadProgressChanged /* 136 */:
                getCurrentTrack(i).flagDefault = j == 1;
                return;
            case NotificationCenter.recordResumed /* 155 */:
                this.blockDurationUs = scaleTimecodeToUs(j);
                return;
            case 159:
                getCurrentTrack(i).channelCount = (int) j;
                return;
            case NotificationCenter.themeUploadError /* 176 */:
                getCurrentTrack(i).width = (int) j;
                return;
            case NotificationCenter.suggestedFiltersLoaded /* 179 */:
                assertInCues(i);
                this.cueTimesUs.add(scaleTimecodeToUs(j));
                return;
            case NotificationCenter.didUpdatePremiumGiftFieldIcon /* 186 */:
                getCurrentTrack(i).height = (int) j;
                return;
            case NotificationCenter.factCheckLoaded /* 215 */:
                getCurrentTrack(i).number = (int) j;
                return;
            case NotificationCenter.emojiLoaded /* 231 */:
                this.clusterTimecodeUs = scaleTimecodeToUs(j);
                return;
            case NotificationCenter.didApplyNewTheme /* 238 */:
                this.blockAdditionalId = (int) j;
                return;
            case NotificationCenter.needShareTheme /* 241 */:
                if (this.seenClusterPositionForCurrentCuePoint) {
                    return;
                }
                assertInCues(i);
                this.cueClusterPositions.add(j);
                this.seenClusterPositionForCurrentCuePoint = true;
                return;
            case NotificationCenter.proxyChangedByRotation /* 251 */:
                this.blockHasReferenceBlock = true;
                return;
            case 16871:
                getCurrentTrack(i).blockAddIdType = (int) j;
                return;
            case 16980:
                if (j == 3) {
                    return;
                }
                throw ParserException.createForMalformedContainer("ContentCompAlgo " + j + " not supported", null);
            case 17029:
                if (j < 1 || j > 2) {
                    throw ParserException.createForMalformedContainer("DocTypeReadVersion " + j + " not supported", null);
                }
                return;
            case 17143:
                if (j == 1) {
                    return;
                }
                throw ParserException.createForMalformedContainer("EBMLReadVersion " + j + " not supported", null);
            case 18401:
                if (j == 5) {
                    return;
                }
                throw ParserException.createForMalformedContainer("ContentEncAlgo " + j + " not supported", null);
            case 18408:
                if (j == 1) {
                    return;
                }
                throw ParserException.createForMalformedContainer("AESSettingsCipherMode " + j + " not supported", null);
            case 21420:
                this.seekEntryPosition = j + this.segmentContentPosition;
                return;
            case 21432:
                int i2 = (int) j;
                assertInTrackEntry(i);
                if (i2 == 0) {
                    this.currentTrack.stereoMode = 0;
                    return;
                }
                if (i2 == 1) {
                    this.currentTrack.stereoMode = 2;
                    return;
                } else if (i2 == 3) {
                    this.currentTrack.stereoMode = 1;
                    return;
                } else {
                    if (i2 != 15) {
                        return;
                    }
                    this.currentTrack.stereoMode = 3;
                    return;
                }
            case 21680:
                getCurrentTrack(i).displayWidth = (int) j;
                return;
            case 21682:
                getCurrentTrack(i).displayUnit = (int) j;
                return;
            case 21690:
                getCurrentTrack(i).displayHeight = (int) j;
                return;
            case 21930:
                getCurrentTrack(i).flagForced = j == 1;
                return;
            case 21998:
                getCurrentTrack(i).maxBlockAdditionId = (int) j;
                return;
            case 22186:
                getCurrentTrack(i).codecDelayNs = j;
                return;
            case 22203:
                getCurrentTrack(i).seekPreRollNs = j;
                return;
            case 25188:
                getCurrentTrack(i).audioBitDepth = (int) j;
                return;
            case 30114:
                this.blockGroupDiscardPaddingNs = j;
                return;
            case 30321:
                assertInTrackEntry(i);
                int i3 = (int) j;
                if (i3 == 0) {
                    this.currentTrack.projectionType = 0;
                    return;
                }
                if (i3 == 1) {
                    this.currentTrack.projectionType = 1;
                    return;
                } else if (i3 == 2) {
                    this.currentTrack.projectionType = 2;
                    return;
                } else {
                    if (i3 != 3) {
                        return;
                    }
                    this.currentTrack.projectionType = 3;
                    return;
                }
            case 2352003:
                getCurrentTrack(i).defaultSampleDurationNs = (int) j;
                return;
            case 2807729:
                this.timecodeScale = j;
                return;
            default:
                switch (i) {
                    case 21945:
                        assertInTrackEntry(i);
                        int i4 = (int) j;
                        if (i4 == 1) {
                            this.currentTrack.colorRange = 2;
                            return;
                        } else {
                            if (i4 != 2) {
                                return;
                            }
                            this.currentTrack.colorRange = 1;
                            return;
                        }
                    case 21946:
                        assertInTrackEntry(i);
                        int isoTransferCharacteristicsToColorTransfer = ColorInfo.isoTransferCharacteristicsToColorTransfer((int) j);
                        if (isoTransferCharacteristicsToColorTransfer != -1) {
                            this.currentTrack.colorTransfer = isoTransferCharacteristicsToColorTransfer;
                            return;
                        }
                        return;
                    case 21947:
                        assertInTrackEntry(i);
                        this.currentTrack.hasColorInfo = true;
                        int isoColorPrimariesToColorSpace = ColorInfo.isoColorPrimariesToColorSpace((int) j);
                        if (isoColorPrimariesToColorSpace != -1) {
                            this.currentTrack.colorSpace = isoColorPrimariesToColorSpace;
                            return;
                        }
                        return;
                    case 21948:
                        getCurrentTrack(i).maxContentLuminance = (int) j;
                        return;
                    case 21949:
                        getCurrentTrack(i).maxFrameAverageLuminance = (int) j;
                        return;
                    default:
                        return;
                }
        }
    }

    protected boolean isLevel1Element(int i) {
        return i == 357149030 || i == 524531317 || i == 475249515 || i == 374648427;
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public final int read(ExtractorInput extractorInput, PositionHolder positionHolder) {
        this.haveOutputSample = false;
        boolean z = true;
        while (z && !this.haveOutputSample) {
            z = this.reader.read(extractorInput);
            if (z && maybeSeekForCues(positionHolder, extractorInput.getPosition())) {
                return 1;
            }
        }
        if (z) {
            return 0;
        }
        for (int i = 0; i < this.tracks.size(); i++) {
            Track track = (Track) this.tracks.valueAt(i);
            track.assertOutputInitialized();
            track.outputPendingSampleMetadata();
        }
        return -1;
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public final void release() {
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public void seek(long j, long j2) {
        this.clusterTimecodeUs = -9223372036854775807L;
        this.blockState = 0;
        this.reader.reset();
        this.varintReader.reset();
        resetWriteSampleData();
        for (int i = 0; i < this.tracks.size(); i++) {
            ((Track) this.tracks.valueAt(i)).reset();
        }
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public final boolean sniff(ExtractorInput extractorInput) {
        return new Sniffer().sniff(extractorInput);
    }

    protected void startMasterElement(int i, long j, long j2) {
        assertInitialized();
        if (i == 160) {
            this.blockHasReferenceBlock = false;
            this.blockGroupDiscardPaddingNs = 0L;
            return;
        }
        if (i == 174) {
            this.currentTrack = new Track();
            return;
        }
        if (i == 187) {
            this.seenClusterPositionForCurrentCuePoint = false;
            return;
        }
        if (i == 19899) {
            this.seekEntryId = -1;
            this.seekEntryPosition = -1L;
            return;
        }
        if (i == 20533) {
            getCurrentTrack(i).hasContentEncryption = true;
            return;
        }
        if (i == 21968) {
            getCurrentTrack(i).hasColorInfo = true;
            return;
        }
        if (i == 408125543) {
            long j3 = this.segmentContentPosition;
            if (j3 != -1 && j3 != j) {
                throw ParserException.createForMalformedContainer("Multiple Segment elements not supported", null);
            }
            this.segmentContentPosition = j;
            this.segmentContentSize = j2;
            return;
        }
        if (i == 475249515) {
            this.cueTimesUs = new LongArray();
            this.cueClusterPositions = new LongArray();
        } else if (i == 524531317 && !this.sentSeekMap) {
            if (this.seekForCuesEnabled && this.cuesContentPosition != -1) {
                this.seekForCues = true;
            } else {
                this.extractorOutput.seekMap(new SeekMap.Unseekable(this.durationUs));
                this.sentSeekMap = true;
            }
        }
    }

    protected void stringElement(int i, String str) {
        if (i == 134) {
            getCurrentTrack(i).codecId = str;
            return;
        }
        if (i != 17026) {
            if (i == 21358) {
                getCurrentTrack(i).name = str;
                return;
            } else {
                if (i != 2274716) {
                    return;
                }
                getCurrentTrack(i).language = str;
                return;
            }
        }
        if ("webm".equals(str) || "matroska".equals(str)) {
            return;
        }
        throw ParserException.createForMalformedContainer("DocType " + str + " not supported", null);
    }
}
