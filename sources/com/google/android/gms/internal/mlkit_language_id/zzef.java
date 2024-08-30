package com.google.android.gms.internal.mlkit_language_id;

import java.util.Collections;
import java.util.Map;
/* loaded from: classes.dex */
public class zzef {
    private static volatile zzef zzc;
    private static final zzef zzd = new zzef(true);
    private final Map zze = Collections.emptyMap();

    private zzef(boolean z) {
    }

    public static zzef zza() {
        zzef zzefVar = zzc;
        if (zzefVar == null) {
            synchronized (zzef.class) {
                try {
                    zzefVar = zzc;
                    if (zzefVar == null) {
                        zzefVar = zzd;
                        zzc = zzefVar;
                    }
                } finally {
                }
            }
        }
        return zzefVar;
    }
}
