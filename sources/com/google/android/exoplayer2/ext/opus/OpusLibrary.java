package com.google.android.exoplayer2.ext.opus;

import com.google.android.exoplayer2.ExoPlayerLibraryInfo;
import com.google.android.exoplayer2.util.LibraryLoader;
/* loaded from: classes.dex */
public final class OpusLibrary {
    private static final LibraryLoader LOADER;
    private static int cryptoType;

    public static native String opusGetVersion();

    public static native boolean opusIsSecureDecodeSupported();

    static {
        ExoPlayerLibraryInfo.registerModule("goog.exo.opus");
        LOADER = new LibraryLoader("opusV2JNI") { // from class: com.google.android.exoplayer2.ext.opus.OpusLibrary.1
            @Override // com.google.android.exoplayer2.util.LibraryLoader
            protected void loadLibrary(String str) {
                System.loadLibrary(str);
            }
        };
        cryptoType = 1;
    }

    private OpusLibrary() {
    }

    public static void setLibraries(int i, String... strArr) {
        cryptoType = i;
        LOADER.setLibraries(strArr);
    }

    public static boolean isAvailable() {
        return LOADER.isAvailable();
    }

    public static String getVersion() {
        if (isAvailable()) {
            return opusGetVersion();
        }
        return null;
    }

    public static boolean supportsCryptoType(int i) {
        if (i != 0) {
            return i != 1 && i == cryptoType;
        }
        return true;
    }
}
