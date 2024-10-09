package com.google.android.exoplayer2.util;

import android.os.Trace;

/* loaded from: classes.dex */
public abstract class TraceUtil {
    public static void beginSection(String str) {
        if (Util.SDK_INT >= 18) {
            beginSectionV18(str);
        }
    }

    private static void beginSectionV18(String str) {
        Trace.beginSection(str);
    }

    public static void endSection() {
        if (Util.SDK_INT >= 18) {
            endSectionV18();
        }
    }

    private static void endSectionV18() {
        Trace.endSection();
    }
}
