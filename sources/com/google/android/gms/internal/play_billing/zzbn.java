package com.google.android.gms.internal.play_billing;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
/* compiled from: com.android.billingclient:billing@@6.0.1 */
/* loaded from: classes.dex */
public final class zzbn {
    static final zzbn zza = new zzbn(true);
    private static volatile zzbn zzd;
    private final Map zze;

    zzbn() {
        this.zze = new HashMap();
    }

    public final zzbz zzb(zzdf zzdfVar, int i) {
        return (zzbz) this.zze.get(new zzbm(zzdfVar, i));
    }

    zzbn(boolean z) {
        this.zze = Collections.emptyMap();
    }

    public static zzbn zza() {
        zzbn zzbnVar = zzd;
        if (zzbnVar != null) {
            return zzbnVar;
        }
        synchronized (zzbn.class) {
            zzbn zzbnVar2 = zzd;
            if (zzbnVar2 != null) {
                return zzbnVar2;
            }
            zzbn zzb = zzbv.zzb(zzbn.class);
            zzd = zzb;
            return zzb;
        }
    }
}
