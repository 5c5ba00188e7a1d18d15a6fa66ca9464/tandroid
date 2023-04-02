package com.google.android.exoplayer2.ext.flac;

import com.google.android.exoplayer2.ExoPlayerLibraryInfo;
import org.telegram.messenger.NativeLoader;
/* loaded from: classes.dex */
public final class FlacLibrary {
    public static final /* synthetic */ int $r8$clinit = 0;

    public static void setLibraries(String... strArr) {
    }

    static {
        ExoPlayerLibraryInfo.registerModule("goog.exo.flac");
    }

    private FlacLibrary() {
    }

    public static boolean isAvailable() {
        return NativeLoader.loaded();
    }
}
