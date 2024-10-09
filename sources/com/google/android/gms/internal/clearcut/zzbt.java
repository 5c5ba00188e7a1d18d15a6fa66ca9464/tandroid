package com.google.android.gms.internal.clearcut;

import java.util.Collections;
import java.util.Map;

/* loaded from: classes.dex */
public final class zzbt {
    private static final Class zzgn = zzam();
    static final zzbt zzgo = new zzbt(true);
    private final Map zzgp = Collections.emptyMap();

    private zzbt(boolean z) {
    }

    private static Class zzam() {
        try {
            return Class.forName("com.google.protobuf.Extension");
        } catch (ClassNotFoundException unused) {
            return null;
        }
    }

    public static zzbt zzan() {
        return zzbs.zzal();
    }
}
