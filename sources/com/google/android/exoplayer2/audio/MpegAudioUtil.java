package com.google.android.exoplayer2.audio;

import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.OneUIUtilities;

/* loaded from: classes.dex */
public abstract class MpegAudioUtil {
    private static final String[] MIME_TYPE_BY_LAYER = {"audio/mpeg-L1", "audio/mpeg-L2", "audio/mpeg"};
    private static final int[] SAMPLING_RATE_V1 = {44100, 48000, 32000};
    private static final int[] BITRATE_V1_L1 = {32000, 64000, 96000, 128000, 160000, 192000, 224000, 256000, 288000, 320000, 352000, 384000, 416000, 448000};
    private static final int[] BITRATE_V2_L1 = {32000, 48000, 56000, 64000, 80000, 96000, 112000, 128000, 144000, 160000, 176000, 192000, 224000, 256000};
    private static final int[] BITRATE_V1_L2 = {32000, 48000, 56000, 64000, 80000, 96000, 112000, 128000, 160000, 192000, 224000, 256000, 320000, 384000};
    private static final int[] BITRATE_V1_L3 = {32000, OneUIUtilities.ONE_UI_4_0, 48000, 56000, 64000, 80000, 96000, 112000, 128000, 160000, 192000, 224000, 256000, 320000};
    private static final int[] BITRATE_V2 = {8000, 16000, 24000, 32000, OneUIUtilities.ONE_UI_4_0, 48000, 56000, 64000, 80000, 96000, 112000, 128000, 144000, 160000};

    /* loaded from: classes.dex */
    public static final class Header {
        public int bitrate;
        public int channels;
        public int frameSize;
        public String mimeType;
        public int sampleRate;
        public int samplesPerFrame;
        public int version;

        /* JADX WARN: Removed duplicated region for block: B:23:0x0052  */
        /* JADX WARN: Removed duplicated region for block: B:29:0x00a8  */
        /* JADX WARN: Removed duplicated region for block: B:33:0x0070  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public boolean setForHeaderData(int i) {
            int i2;
            int i3;
            int i4;
            int i5;
            int i6;
            int i7;
            if (!MpegAudioUtil.isMagicPresent(i) || (i2 = (i >>> 19) & 3) == 1 || (i3 = (i >>> 17) & 3) == 0 || (i4 = (i >>> 12) & 15) == 0 || i4 == 15 || (i5 = (i >>> 10) & 3) == 3) {
                return false;
            }
            this.version = i2;
            this.mimeType = MpegAudioUtil.MIME_TYPE_BY_LAYER[3 - i3];
            int i8 = MpegAudioUtil.SAMPLING_RATE_V1[i5];
            this.sampleRate = i8;
            if (i2 != 2) {
                if (i2 == 0) {
                    i6 = i8 / 4;
                }
                int i9 = (i >>> 9) & 1;
                this.samplesPerFrame = MpegAudioUtil.getFrameSizeInSamples(i2, i3);
                if (i3 != 3) {
                    int i10 = i2 == 3 ? MpegAudioUtil.BITRATE_V1_L1[i4 - 1] : MpegAudioUtil.BITRATE_V2_L1[i4 - 1];
                    this.bitrate = i10;
                    i7 = (((i10 * 12) / this.sampleRate) + i9) * 4;
                } else {
                    int i11 = NotificationCenter.messagePlayingProgressDidChanged;
                    if (i2 != 3) {
                        int i12 = MpegAudioUtil.BITRATE_V2[i4 - 1];
                        this.bitrate = i12;
                        if (i3 == 1) {
                            i11 = 72;
                        }
                        this.frameSize = ((i11 * i12) / this.sampleRate) + i9;
                        this.channels = ((i >> 6) & 3) == 3 ? 1 : 2;
                        return true;
                    }
                    int i13 = i3 == 2 ? MpegAudioUtil.BITRATE_V1_L2[i4 - 1] : MpegAudioUtil.BITRATE_V1_L3[i4 - 1];
                    this.bitrate = i13;
                    i7 = ((i13 * NotificationCenter.messagePlayingProgressDidChanged) / this.sampleRate) + i9;
                }
                this.frameSize = i7;
                this.channels = ((i >> 6) & 3) == 3 ? 1 : 2;
                return true;
            }
            i6 = i8 / 2;
            this.sampleRate = i6;
            int i92 = (i >>> 9) & 1;
            this.samplesPerFrame = MpegAudioUtil.getFrameSizeInSamples(i2, i3);
            if (i3 != 3) {
            }
            this.frameSize = i7;
            this.channels = ((i >> 6) & 3) == 3 ? 1 : 2;
            return true;
        }
    }

    public static int getFrameSize(int i) {
        int i2;
        int i3;
        int i4;
        int i5;
        if (!isMagicPresent(i) || (i2 = (i >>> 19) & 3) == 1 || (i3 = (i >>> 17) & 3) == 0 || (i4 = (i >>> 12) & 15) == 0 || i4 == 15 || (i5 = (i >>> 10) & 3) == 3) {
            return -1;
        }
        int i6 = SAMPLING_RATE_V1[i5];
        if (i2 == 2) {
            i6 /= 2;
        } else if (i2 == 0) {
            i6 /= 4;
        }
        int i7 = (i >>> 9) & 1;
        if (i3 == 3) {
            return ((((i2 == 3 ? BITRATE_V1_L1[i4 - 1] : BITRATE_V2_L1[i4 - 1]) * 12) / i6) + i7) * 4;
        }
        int i8 = i2 == 3 ? i3 == 2 ? BITRATE_V1_L2[i4 - 1] : BITRATE_V1_L3[i4 - 1] : BITRATE_V2[i4 - 1];
        int i9 = NotificationCenter.messagePlayingProgressDidChanged;
        if (i2 == 3) {
            return ((i8 * NotificationCenter.messagePlayingProgressDidChanged) / i6) + i7;
        }
        if (i3 == 1) {
            i9 = 72;
        }
        return ((i9 * i8) / i6) + i7;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getFrameSizeInSamples(int i, int i2) {
        if (i2 == 1) {
            return i == 3 ? 1152 : 576;
        }
        if (i2 == 2) {
            return 1152;
        }
        if (i2 == 3) {
            return 384;
        }
        throw new IllegalArgumentException();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isMagicPresent(int i) {
        return (i & (-2097152)) == -2097152;
    }

    public static int parseMpegAudioFrameSampleCount(int i) {
        int i2;
        int i3;
        if (!isMagicPresent(i) || (i2 = (i >>> 19) & 3) == 1 || (i3 = (i >>> 17) & 3) == 0) {
            return -1;
        }
        int i4 = (i >>> 12) & 15;
        int i5 = (i >>> 10) & 3;
        if (i4 == 0 || i4 == 15 || i5 == 3) {
            return -1;
        }
        return getFrameSizeInSamples(i2, i3);
    }
}
