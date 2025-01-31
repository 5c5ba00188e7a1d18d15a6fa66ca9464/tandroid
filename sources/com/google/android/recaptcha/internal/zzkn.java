package com.google.android.recaptcha.internal;

import j$.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/* loaded from: classes.dex */
final class zzkn {
    public static final /* synthetic */ int zza = 0;
    private static final zzkn zzb = new zzkn();
    private final ConcurrentMap zzd = new ConcurrentHashMap();
    private final zzks zzc = new zzjw();

    private zzkn() {
    }

    public static zzkn zza() {
        return zzb;
    }

    public final zzkr zzb(Class cls) {
        zzjc.zzc(cls, "messageType");
        zzkr zzkrVar = (zzkr) this.zzd.get(cls);
        if (zzkrVar == null) {
            zzkrVar = this.zzc.zza(cls);
            zzjc.zzc(cls, "messageType");
            zzkr zzkrVar2 = (zzkr) this.zzd.putIfAbsent(cls, zzkrVar);
            if (zzkrVar2 != null) {
                return zzkrVar2;
            }
        }
        return zzkrVar;
    }
}
