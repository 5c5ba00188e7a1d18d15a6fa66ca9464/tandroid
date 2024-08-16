package com.google.android.gms.internal.mlkit_language_id;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
/* compiled from: com.google.mlkit:language-id@@16.1.1 */
/* loaded from: classes.dex */
public class zzef {
    private static volatile zzef zzc;
    private static final zzef zzd = new zzef(true);
    private final Map<Object, Object> zze;

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

    zzef() {
        this.zze = new HashMap();
    }

    private zzef(boolean z) {
        this.zze = Collections.emptyMap();
    }
}
