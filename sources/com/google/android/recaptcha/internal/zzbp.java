package com.google.android.recaptcha.internal;

import java.util.concurrent.TimeUnit;

/* loaded from: classes.dex */
public final class zzbp {
    public static final zzbp zza = new zzbp();

    private zzbp() {
    }

    public static final zzno zza(zzz zzzVar, zzz zzzVar2) {
        zznn zzf = zzno.zzf();
        zzf.zzp(zzmg.zzb(zzzVar.zzb()));
        TimeUnit timeUnit = TimeUnit.NANOSECONDS;
        zzf.zzq(zzme.zza(zzzVar.zza(timeUnit)));
        zzf.zzd(zzmg.zzb(zzzVar2.zzb()));
        zzf.zze(zzme.zza(zzzVar2.zza(timeUnit)));
        return (zzno) zzf.zzj();
    }
}
