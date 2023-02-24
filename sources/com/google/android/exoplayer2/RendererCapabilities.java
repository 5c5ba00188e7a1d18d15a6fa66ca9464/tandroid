package com.google.android.exoplayer2;

import android.annotation.SuppressLint;
/* loaded from: classes.dex */
public interface RendererCapabilities {
    String getName();

    int getTrackType();

    int supportsFormat(Format format) throws ExoPlaybackException;

    int supportsMixedMimeTypeAdaptation() throws ExoPlaybackException;

    /* loaded from: classes.dex */
    public final /* synthetic */ class -CC {
        @SuppressLint({"WrongConstant"})
        public static int create(int i, int i2, int i3, int i4, int i5) {
            return i | i2 | i3 | i4 | i5;
        }

        @SuppressLint({"WrongConstant"})
        public static int getAdaptiveSupport(int i) {
            return i & 24;
        }

        @SuppressLint({"WrongConstant"})
        public static int getDecoderSupport(int i) {
            return i & 384;
        }

        @SuppressLint({"WrongConstant"})
        public static int getFormatSupport(int i) {
            return i & 7;
        }

        @SuppressLint({"WrongConstant"})
        public static int getHardwareAccelerationSupport(int i) {
            return i & 64;
        }

        @SuppressLint({"WrongConstant"})
        public static int getTunnelingSupport(int i) {
            return i & 32;
        }

        public static int create(int i) {
            return create(i, 0, 0);
        }

        public static int create(int i, int i2, int i3) {
            return create(i, i2, i3, 0, 128);
        }
    }
}
