package com.google.android.exoplayer2;

/* loaded from: classes.dex */
public interface RendererCapabilities {

    public abstract /* synthetic */ class -CC {
        public static int create(int i) {
            return create(i, 0, 0);
        }

        public static int create(int i, int i2, int i3) {
            return create(i, i2, i3, 0, 128);
        }

        public static int create(int i, int i2, int i3, int i4, int i5) {
            return i | i2 | i3 | i4 | i5;
        }

        public static int getAdaptiveSupport(int i) {
            return i & 24;
        }

        public static int getDecoderSupport(int i) {
            return i & 384;
        }

        public static int getFormatSupport(int i) {
            return i & 7;
        }

        public static int getHardwareAccelerationSupport(int i) {
            return i & 64;
        }

        public static int getTunnelingSupport(int i) {
            return i & 32;
        }
    }

    String getName();

    int getTrackType();

    int supportsFormat(Format format);

    int supportsMixedMimeTypeAdaptation();
}
