package com.google.android.gms.internal.mlkit_language_id;

import j$.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzgk {
    private static final zzgk zza = new zzgk();
    private final ConcurrentMap zzc = new ConcurrentHashMap();
    private final zzgo zzb = new zzfm();

    private zzgk() {
    }

    public static zzgk zza() {
        return zza;
    }

    public final zzgp zza(Class cls) {
        zzeq.zza((Object) cls, "messageType");
        zzgp zzgpVar = (zzgp) this.zzc.get(cls);
        if (zzgpVar == null) {
            zzgp zza2 = this.zzb.zza(cls);
            zzeq.zza((Object) cls, "messageType");
            zzeq.zza((Object) zza2, "schema");
            zzgp zzgpVar2 = (zzgp) this.zzc.putIfAbsent(cls, zza2);
            return zzgpVar2 != null ? zzgpVar2 : zza2;
        }
        return zzgpVar;
    }

    public final zzgp zza(Object obj) {
        return zza((Class) obj.getClass());
    }
}
