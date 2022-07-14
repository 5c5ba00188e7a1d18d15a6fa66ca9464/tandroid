package com.google.android.exoplayer2.audio;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.ts.PsExtractor;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.ParsableBitArray;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.gms.location.LocationRequest;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;
import org.telegram.ui.GroupCallActivity;
/* loaded from: classes3.dex */
public final class Ac3Util {
    private static final int AC3_SYNCFRAME_AUDIO_SAMPLE_COUNT = 1536;
    private static final int AUDIO_SAMPLES_PER_AUDIO_BLOCK = 256;
    public static final int TRUEHD_RECHUNK_SAMPLE_COUNT = 16;
    public static final int TRUEHD_SYNCFRAME_PREFIX_LENGTH = 10;
    private static final int[] BLOCKS_PER_SYNCFRAME_BY_NUMBLKSCOD = {1, 2, 3, 6};
    private static final int[] SAMPLE_RATE_BY_FSCOD = {48000, 44100, 32000};
    private static final int[] SAMPLE_RATE_BY_FSCOD2 = {24000, 22050, 16000};
    private static final int[] CHANNEL_COUNT_BY_ACMOD = {2, 1, 2, 3, 3, 4, 4, 5};
    private static final int[] BITRATE_BY_HALF_FRMSIZECOD = {32, 40, 48, 56, 64, 80, 96, 112, 128, 160, PsExtractor.AUDIO_STREAM, 224, 256, GroupCallActivity.TABLET_LIST_SIZE, 384, 448, 512, 576, 640};
    private static final int[] SYNCFRAME_SIZE_WORDS_BY_HALF_FRMSIZECOD_44_1 = {69, 87, LocationRequest.PRIORITY_LOW_POWER, 121, 139, 174, 208, 243, 278, 348, 417, 487, 557, 696, 835, 975, 1114, 1253, 1393};

    /* loaded from: classes3.dex */
    public static final class SyncFrameInfo {
        public static final int STREAM_TYPE_TYPE0 = 0;
        public static final int STREAM_TYPE_TYPE1 = 1;
        public static final int STREAM_TYPE_TYPE2 = 2;
        public static final int STREAM_TYPE_UNDEFINED = -1;
        public final int channelCount;
        public final int frameSize;
        public final String mimeType;
        public final int sampleCount;
        public final int sampleRate;
        public final int streamType;

        @Documented
        @Retention(RetentionPolicy.SOURCE)
        /* loaded from: classes.dex */
        public @interface StreamType {
        }

        private SyncFrameInfo(String mimeType, int streamType, int channelCount, int sampleRate, int frameSize, int sampleCount) {
            this.mimeType = mimeType;
            this.streamType = streamType;
            this.channelCount = channelCount;
            this.sampleRate = sampleRate;
            this.frameSize = frameSize;
            this.sampleCount = sampleCount;
        }
    }

    public static Format parseAc3AnnexFFormat(ParsableByteArray data, String trackId, String language, DrmInitData drmInitData) {
        int channelCount;
        int fscod = (data.readUnsignedByte() & PsExtractor.AUDIO_STREAM) >> 6;
        int sampleRate = SAMPLE_RATE_BY_FSCOD[fscod];
        int nextByte = data.readUnsignedByte();
        int channelCount2 = CHANNEL_COUNT_BY_ACMOD[(nextByte & 56) >> 3];
        if ((nextByte & 4) == 0) {
            channelCount = channelCount2;
        } else {
            channelCount = channelCount2 + 1;
        }
        return Format.createAudioSampleFormat(trackId, MimeTypes.AUDIO_AC3, null, -1, -1, channelCount, sampleRate, null, drmInitData, 0, language);
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0043  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0057  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static Format parseEAc3AnnexFFormat(ParsableByteArray data, String trackId, String language, DrmInitData drmInitData) {
        int channelCount;
        String mimeType;
        data.skipBytes(2);
        int fscod = (data.readUnsignedByte() & PsExtractor.AUDIO_STREAM) >> 6;
        int sampleRate = SAMPLE_RATE_BY_FSCOD[fscod];
        int nextByte = data.readUnsignedByte();
        int channelCount2 = CHANNEL_COUNT_BY_ACMOD[(nextByte & 14) >> 1];
        if ((nextByte & 1) != 0) {
            channelCount2++;
        }
        int numDepSub = (data.readUnsignedByte() & 30) >> 1;
        if (numDepSub > 0) {
            int lowByteChanLoc = data.readUnsignedByte();
            if ((lowByteChanLoc & 2) != 0) {
                channelCount = channelCount2 + 2;
                if (data.bytesLeft() <= 0) {
                    mimeType = (data.readUnsignedByte() & 1) != 0 ? MimeTypes.AUDIO_E_AC3_JOC : MimeTypes.AUDIO_E_AC3;
                } else {
                    mimeType = MimeTypes.AUDIO_E_AC3;
                }
                return Format.createAudioSampleFormat(trackId, mimeType, null, -1, -1, channelCount, sampleRate, null, drmInitData, 0, language);
            }
        }
        channelCount = channelCount2;
        if (data.bytesLeft() <= 0) {
        }
        return Format.createAudioSampleFormat(trackId, mimeType, null, -1, -1, channelCount, sampleRate, null, drmInitData, 0, language);
    }

    public static SyncFrameInfo parseAc3SyncframeInfo(ParsableBitArray data) {
        int channelCount;
        int sampleRate;
        int frameSize;
        int sampleCount;
        String mimeType;
        int i;
        int numblkscod;
        int audioBlocks;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int initialPosition = data.getPosition();
        data.skipBits(40);
        boolean isEac3 = data.readBits(5) > 10;
        data.setPosition(initialPosition);
        int streamType = -1;
        if (isEac3) {
            data.skipBits(16);
            switch (data.readBits(2)) {
                case 0:
                    streamType = 0;
                    break;
                case 1:
                    streamType = 1;
                    break;
                case 2:
                    streamType = 2;
                    break;
                default:
                    streamType = -1;
                    break;
            }
            data.skipBits(3);
            frameSize = (data.readBits(11) + 1) * 2;
            int fscod = data.readBits(2);
            if (fscod == 3) {
                numblkscod = 3;
                sampleRate = SAMPLE_RATE_BY_FSCOD2[data.readBits(2)];
                audioBlocks = 6;
            } else {
                numblkscod = data.readBits(2);
                int audioBlocks2 = BLOCKS_PER_SYNCFRAME_BY_NUMBLKSCOD[numblkscod];
                sampleRate = SAMPLE_RATE_BY_FSCOD[fscod];
                audioBlocks = audioBlocks2;
            }
            sampleCount = audioBlocks * 256;
            int acmod = data.readBits(3);
            boolean lfeon = data.readBit();
            channelCount = CHANNEL_COUNT_BY_ACMOD[acmod] + (lfeon ? 1 : 0);
            data.skipBits(10);
            if (data.readBit()) {
                data.skipBits(8);
            }
            if (acmod == 0) {
                data.skipBits(5);
                if (data.readBit()) {
                    data.skipBits(8);
                }
            }
            if (streamType == 1 && data.readBit()) {
                data.skipBits(16);
            }
            if (data.readBit()) {
                if (acmod > 2) {
                    data.skipBits(2);
                }
                if ((acmod & 1) == 0 || acmod <= 2) {
                    i5 = 6;
                } else {
                    i5 = 6;
                    data.skipBits(6);
                }
                if ((acmod & 4) != 0) {
                    data.skipBits(i5);
                }
                if (lfeon && data.readBit()) {
                    data.skipBits(5);
                }
                if (streamType == 0) {
                    if (!data.readBit()) {
                        i6 = 6;
                    } else {
                        i6 = 6;
                        data.skipBits(6);
                    }
                    if (acmod == 0 && data.readBit()) {
                        data.skipBits(i6);
                    }
                    if (data.readBit()) {
                        data.skipBits(i6);
                    }
                    int mixdef = data.readBits(2);
                    if (mixdef == 1) {
                        data.skipBits(5);
                    } else if (mixdef == 2) {
                        data.skipBits(12);
                    } else if (mixdef == 3) {
                        int mixdeflen = data.readBits(5);
                        if (data.readBit()) {
                            data.skipBits(5);
                            if (!data.readBit()) {
                                i7 = 4;
                            } else {
                                i7 = 4;
                                data.skipBits(4);
                            }
                            if (data.readBit()) {
                                data.skipBits(i7);
                            }
                            if (data.readBit()) {
                                data.skipBits(i7);
                            }
                            if (data.readBit()) {
                                data.skipBits(i7);
                            }
                            if (data.readBit()) {
                                data.skipBits(i7);
                            }
                            if (data.readBit()) {
                                data.skipBits(i7);
                            }
                            if (data.readBit()) {
                                data.skipBits(i7);
                            }
                            if (data.readBit()) {
                                if (data.readBit()) {
                                    data.skipBits(i7);
                                }
                                if (data.readBit()) {
                                    data.skipBits(i7);
                                }
                            }
                        }
                        if (data.readBit()) {
                            data.skipBits(5);
                            if (data.readBit()) {
                                data.skipBits(7);
                                if (data.readBit()) {
                                    data.skipBits(8);
                                }
                            }
                        }
                        data.skipBits((mixdeflen + 2) * 8);
                        data.byteAlign();
                    }
                    if (acmod < 2) {
                        if (data.readBit()) {
                            data.skipBits(14);
                        }
                        if (acmod == 0 && data.readBit()) {
                            data.skipBits(14);
                        }
                    }
                    if (data.readBit()) {
                        if (numblkscod == 0) {
                            data.skipBits(5);
                        } else {
                            for (int blk = 0; blk < audioBlocks; blk++) {
                                if (data.readBit()) {
                                    data.skipBits(5);
                                }
                            }
                        }
                    }
                }
            }
            if (!data.readBit()) {
                i2 = 3;
            } else {
                data.skipBits(5);
                if (acmod == 2) {
                    data.skipBits(4);
                }
                if (acmod >= 6) {
                    data.skipBits(2);
                }
                if (!data.readBit()) {
                    i4 = 8;
                } else {
                    i4 = 8;
                    data.skipBits(8);
                }
                if (acmod == 0 && data.readBit()) {
                    data.skipBits(i4);
                }
                i2 = 3;
                if (fscod < 3) {
                    data.skipBit();
                }
            }
            if (streamType == 0 && numblkscod != i2) {
                data.skipBit();
            }
            if (streamType != 2) {
                i3 = 6;
            } else if (numblkscod == i2 || data.readBit()) {
                i3 = 6;
                data.skipBits(6);
            } else {
                i3 = 6;
            }
            mimeType = MimeTypes.AUDIO_E_AC3;
            if (data.readBit()) {
                int addbsil = data.readBits(i3);
                if (addbsil == 1 && data.readBits(8) == 1) {
                    mimeType = MimeTypes.AUDIO_E_AC3_JOC;
                }
            }
        } else {
            data.skipBits(32);
            int fscod2 = data.readBits(2);
            if (fscod2 != 3) {
                mimeType = MimeTypes.AUDIO_AC3;
            } else {
                mimeType = null;
            }
            int frmsizecod = data.readBits(6);
            frameSize = getAc3SyncframeSize(fscod2, frmsizecod);
            data.skipBits(8);
            int acmod2 = data.readBits(3);
            if ((acmod2 & 1) == 0 || acmod2 == 1) {
                i = 2;
            } else {
                i = 2;
                data.skipBits(2);
            }
            if ((acmod2 & 4) != 0) {
                data.skipBits(i);
            }
            if (acmod2 == i) {
                data.skipBits(i);
            }
            int[] iArr = SAMPLE_RATE_BY_FSCOD;
            sampleRate = fscod2 < iArr.length ? iArr[fscod2] : -1;
            sampleCount = AC3_SYNCFRAME_AUDIO_SAMPLE_COUNT;
            channelCount = CHANNEL_COUNT_BY_ACMOD[acmod2] + (data.readBit() ? 1 : 0);
        }
        return new SyncFrameInfo(mimeType, streamType, channelCount, sampleRate, frameSize, sampleCount);
    }

    public static int parseAc3SyncframeSize(byte[] data) {
        if (data.length < 6) {
            return -1;
        }
        boolean isEac3 = ((data[5] & 248) >> 3) > 10;
        if (isEac3) {
            int frmsiz = (data[2] & 7) << 8;
            return (((data[3] & 255) | frmsiz) + 1) * 2;
        }
        int fscod = (data[4] & 192) >> 6;
        int frmsizecod = data[4] & 63;
        return getAc3SyncframeSize(fscod, frmsizecod);
    }

    public static int parseAc3SyncframeAudioSampleCount(ByteBuffer buffer) {
        int numblkscod = 3;
        boolean isEac3 = ((buffer.get(buffer.position() + 5) & 248) >> 3) > 10;
        if (isEac3) {
            int fscod = (buffer.get(buffer.position() + 4) & 192) >> 6;
            if (fscod != 3) {
                numblkscod = (buffer.get(buffer.position() + 4) & 48) >> 4;
            }
            return BLOCKS_PER_SYNCFRAME_BY_NUMBLKSCOD[numblkscod] * 256;
        }
        return AC3_SYNCFRAME_AUDIO_SAMPLE_COUNT;
    }

    public static int findTrueHdSyncframeOffset(ByteBuffer buffer) {
        int startIndex = buffer.position();
        int endIndex = buffer.limit() - 10;
        for (int i = startIndex; i <= endIndex; i++) {
            if ((buffer.getInt(i + 4) & (-16777217)) == -1167101192) {
                return i - startIndex;
            }
        }
        return -1;
    }

    public static int parseTrueHdSyncframeAudioSampleCount(byte[] syncframe) {
        boolean isMlp = false;
        if (syncframe[4] == -8 && syncframe[5] == 114 && syncframe[6] == 111 && (syncframe[7] & 254) == 186) {
            if ((syncframe[7] & 255) == 187) {
                isMlp = true;
            }
            return 40 << ((syncframe[isMlp ? '\t' : '\b'] >> 4) & 7);
        }
        return 0;
    }

    public static int parseTrueHdSyncframeAudioSampleCount(ByteBuffer buffer, int offset) {
        boolean isMlp = (buffer.get((buffer.position() + offset) + 7) & 255) == 187;
        return 40 << ((buffer.get((buffer.position() + offset) + (isMlp ? 9 : 8)) >> 4) & 7);
    }

    private static int getAc3SyncframeSize(int fscod, int frmsizecod) {
        int halfFrmsizecod = frmsizecod / 2;
        if (fscod >= 0) {
            int[] iArr = SAMPLE_RATE_BY_FSCOD;
            if (fscod >= iArr.length || frmsizecod < 0) {
                return -1;
            }
            int[] iArr2 = SYNCFRAME_SIZE_WORDS_BY_HALF_FRMSIZECOD_44_1;
            if (halfFrmsizecod >= iArr2.length) {
                return -1;
            }
            int sampleRate = iArr[fscod];
            if (sampleRate == 44100) {
                return (iArr2[halfFrmsizecod] + (frmsizecod % 2)) * 2;
            }
            int bitrate = BITRATE_BY_HALF_FRMSIZECOD[halfFrmsizecod];
            if (sampleRate == 32000) {
                return bitrate * 6;
            }
            return bitrate * 4;
        }
        return -1;
    }

    private Ac3Util() {
    }
}
