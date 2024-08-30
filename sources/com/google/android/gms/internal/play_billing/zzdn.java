package com.google.android.gms.internal.play_billing;

import j$.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzdn {
    private static final zzdn zza = new zzdn();
    private final ConcurrentMap zzc = new ConcurrentHashMap();
    private final zzdq zzb = new zzcx();

    private zzdn() {
    }

    public static zzdn zza() {
        return zza;
    }

    public final zzdp zzb(Class cls) {
        zzcg.zzc(cls, "messageType");
        zzdp zzdpVar = (zzdp) this.zzc.get(cls);
        if (zzdpVar == null) {
            zzdpVar = this.zzb.zza(cls);
            zzcg.zzc(cls, "messageType");
            zzdp zzdpVar2 = (zzdp) this.zzc.putIfAbsent(cls, zzdpVar);
            if (zzdpVar2 != null) {
                return zzdpVar2;
            }
        }
        return zzdpVar;
    }
}
