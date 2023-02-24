package com.google.android.exoplayer2.ext.flac;

import com.google.android.exoplayer2.ExoPlayerLibraryInfo;
import com.google.android.exoplayer2.util.LibraryLoader;
/* loaded from: classes.dex */
public final class FlacLibrary {
    public static final /* synthetic */ int $r8$clinit = 0;
    private static final LibraryLoader LOADER;

    static {
        ExoPlayerLibraryInfo.registerModule("goog.exo.flac");
        LOADER = new LibraryLoader("flacJNI") { // from class: com.google.android.exoplayer2.ext.flac.FlacLibrary.1
            @Override // com.google.android.exoplayer2.util.LibraryLoader
            protected void loadLibrary(String str) {
                System.loadLibrary(str);
            }
        };
    }

    private FlacLibrary() {
    }

    public static void setLibraries(String... strArr) {
        LOADER.setLibraries(strArr);
    }

    public static boolean isAvailable() {
        return LOADER.isAvailable();
    }
}
