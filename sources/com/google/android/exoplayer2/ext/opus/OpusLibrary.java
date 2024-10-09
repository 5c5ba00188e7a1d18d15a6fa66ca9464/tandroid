package com.google.android.exoplayer2.ext.opus;

import com.google.android.exoplayer2.ExoPlayerLibraryInfo;
import org.telegram.messenger.NativeLoader;

/* loaded from: classes.dex */
public final class OpusLibrary {
    private static int cryptoType;

    static {
        ExoPlayerLibraryInfo.registerModule("goog.exo.opus");
        cryptoType = 1;
    }

    private OpusLibrary() {
    }

    public static String getVersion() {
        if (isAvailable()) {
            return opusGetVersion();
        }
        return null;
    }

    public static boolean isAvailable() {
        return NativeLoader.loaded();
    }

    public static native String opusGetVersion();

    public static native boolean opusIsSecureDecodeSupported();

    public static void setLibraries(int i, String... strArr) {
        cryptoType = i;
    }

    public static boolean supportsCryptoType(int i) {
        if (i != 0) {
            return i != 1 && i == cryptoType;
        }
        return true;
    }
}
