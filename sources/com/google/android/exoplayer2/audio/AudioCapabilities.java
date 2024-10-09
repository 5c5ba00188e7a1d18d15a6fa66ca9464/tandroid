package com.google.android.exoplayer2.audio;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioTrack;
import android.provider.Settings;
import android.util.Pair;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.primitives.Ints;
import java.util.Arrays;

/* loaded from: classes.dex */
public final class AudioCapabilities {
    private final int maxChannelCount;
    private final int[] supportedEncodings;
    public static final AudioCapabilities DEFAULT_AUDIO_CAPABILITIES = new AudioCapabilities(new int[]{2}, 8);
    private static final AudioCapabilities EXTERNAL_SURROUND_SOUND_CAPABILITIES = new AudioCapabilities(new int[]{2, 5, 6}, 8);
    private static final ImmutableMap ALL_SURROUND_ENCODINGS_AND_MAX_CHANNELS = new ImmutableMap.Builder().put(5, 6).put(17, 6).put(7, 6).put(18, 6).put(6, 8).put(8, 8).put(14, 8).buildOrThrow();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class Api29 {
        private static final android.media.AudioAttributes DEFAULT_AUDIO_ATTRIBUTES;

        static {
            AudioAttributes.Builder usage;
            AudioAttributes.Builder contentType;
            AudioAttributes.Builder flags;
            android.media.AudioAttributes build;
            usage = new AudioAttributes.Builder().setUsage(1);
            contentType = usage.setContentType(3);
            flags = contentType.setFlags(0);
            build = flags.build();
            DEFAULT_AUDIO_ATTRIBUTES = build;
        }

        public static int[] getDirectPlaybackSupportedEncodings() {
            AudioFormat.Builder channelMask;
            AudioFormat.Builder encoding;
            AudioFormat.Builder sampleRate;
            AudioFormat build;
            boolean isDirectPlaybackSupported;
            ImmutableList.Builder builder = ImmutableList.builder();
            UnmodifiableIterator it = AudioCapabilities.ALL_SURROUND_ENCODINGS_AND_MAX_CHANNELS.keySet().iterator();
            while (it.hasNext()) {
                Integer num = (Integer) it.next();
                int intValue = num.intValue();
                channelMask = new AudioFormat.Builder().setChannelMask(12);
                encoding = channelMask.setEncoding(intValue);
                sampleRate = encoding.setSampleRate(48000);
                build = sampleRate.build();
                isDirectPlaybackSupported = AudioTrack.isDirectPlaybackSupported(build, DEFAULT_AUDIO_ATTRIBUTES);
                if (isDirectPlaybackSupported) {
                    builder.add((Object) num);
                }
            }
            builder.add((Object) 2);
            return Ints.toArray(builder.build());
        }

        public static int getMaxSupportedChannelCountForPassthrough(int i, int i2) {
            AudioFormat.Builder encoding;
            AudioFormat.Builder sampleRate;
            AudioFormat.Builder channelMask;
            AudioFormat build;
            boolean isDirectPlaybackSupported;
            for (int i3 = 8; i3 > 0; i3--) {
                encoding = new AudioFormat.Builder().setEncoding(i);
                sampleRate = encoding.setSampleRate(i2);
                channelMask = sampleRate.setChannelMask(Util.getAudioTrackChannelConfig(i3));
                build = channelMask.build();
                isDirectPlaybackSupported = AudioTrack.isDirectPlaybackSupported(build, DEFAULT_AUDIO_ATTRIBUTES);
                if (isDirectPlaybackSupported) {
                    return i3;
                }
            }
            return 0;
        }
    }

    public AudioCapabilities(int[] iArr, int i) {
        if (iArr != null) {
            int[] copyOf = Arrays.copyOf(iArr, iArr.length);
            this.supportedEncodings = copyOf;
            Arrays.sort(copyOf);
        } else {
            this.supportedEncodings = new int[0];
        }
        this.maxChannelCount = i;
    }

    private static boolean deviceMaySetExternalSurroundSoundGlobalSetting() {
        if (Util.SDK_INT >= 17) {
            String str = Util.MANUFACTURER;
            if ("Amazon".equals(str) || "Xiaomi".equals(str)) {
                return true;
            }
        }
        return false;
    }

    public static AudioCapabilities getCapabilities(Context context) {
        return getCapabilities(context, Util.registerReceiverNotExported(context, null, new IntentFilter("android.media.action.HDMI_AUDIO_PLUG")));
    }

    static AudioCapabilities getCapabilities(Context context, Intent intent) {
        return (deviceMaySetExternalSurroundSoundGlobalSetting() && Settings.Global.getInt(context.getContentResolver(), "external_surround_sound_enabled", 0) == 1) ? EXTERNAL_SURROUND_SOUND_CAPABILITIES : (Util.SDK_INT < 29 || !(Util.isTv(context) || Util.isAutomotive(context))) ? (intent == null || intent.getIntExtra("android.media.extra.AUDIO_PLUG_STATE", 0) == 0) ? DEFAULT_AUDIO_CAPABILITIES : new AudioCapabilities(intent.getIntArrayExtra("android.media.extra.ENCODINGS"), intent.getIntExtra("android.media.extra.MAX_CHANNEL_COUNT", 8)) : new AudioCapabilities(Api29.getDirectPlaybackSupportedEncodings(), 8);
    }

    private static int getChannelConfigForPassthrough(int i) {
        int i2 = Util.SDK_INT;
        if (i2 <= 28) {
            if (i == 7) {
                i = 8;
            } else if (i == 3 || i == 4 || i == 5) {
                i = 6;
            }
        }
        if (i2 <= 26 && "fugu".equals(Util.DEVICE) && i == 1) {
            i = 2;
        }
        return Util.getAudioTrackChannelConfig(i);
    }

    private static int getMaxSupportedChannelCountForPassthrough(int i, int i2) {
        return Util.SDK_INT >= 29 ? Api29.getMaxSupportedChannelCountForPassthrough(i, i2) : ((Integer) Assertions.checkNotNull((Integer) ALL_SURROUND_ENCODINGS_AND_MAX_CHANNELS.getOrDefault(Integer.valueOf(i), 0))).intValue();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AudioCapabilities)) {
            return false;
        }
        AudioCapabilities audioCapabilities = (AudioCapabilities) obj;
        return Arrays.equals(this.supportedEncodings, audioCapabilities.supportedEncodings) && this.maxChannelCount == audioCapabilities.maxChannelCount;
    }

    public Pair getEncodingAndChannelConfigForPassthrough(Format format) {
        int encoding = MimeTypes.getEncoding((String) Assertions.checkNotNull(format.sampleMimeType), format.codecs);
        if (!ALL_SURROUND_ENCODINGS_AND_MAX_CHANNELS.containsKey(Integer.valueOf(encoding))) {
            return null;
        }
        if (encoding == 18 && !supportsEncoding(18)) {
            encoding = 6;
        } else if (encoding == 8 && !supportsEncoding(8)) {
            encoding = 7;
        }
        if (!supportsEncoding(encoding)) {
            return null;
        }
        int i = format.channelCount;
        if (i == -1 || encoding == 18) {
            int i2 = format.sampleRate;
            if (i2 == -1) {
                i2 = 48000;
            }
            i = getMaxSupportedChannelCountForPassthrough(encoding, i2);
        } else if (i > this.maxChannelCount) {
            return null;
        }
        int channelConfigForPassthrough = getChannelConfigForPassthrough(i);
        if (channelConfigForPassthrough == 0) {
            return null;
        }
        return Pair.create(Integer.valueOf(encoding), Integer.valueOf(channelConfigForPassthrough));
    }

    public int hashCode() {
        return this.maxChannelCount + (Arrays.hashCode(this.supportedEncodings) * 31);
    }

    public boolean isPassthroughPlaybackSupported(Format format) {
        return getEncodingAndChannelConfigForPassthrough(format) != null;
    }

    public boolean supportsEncoding(int i) {
        return Arrays.binarySearch(this.supportedEncodings, i) >= 0;
    }

    public String toString() {
        return "AudioCapabilities[maxChannelCount=" + this.maxChannelCount + ", supportedEncodings=" + Arrays.toString(this.supportedEncodings) + "]";
    }
}
