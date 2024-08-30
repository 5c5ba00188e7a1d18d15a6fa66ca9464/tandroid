package com.google.android.gms.internal.icing;

import com.google.android.gms.common.api.Api;
/* loaded from: classes.dex */
public abstract class zze {
    public static final Api.ClientKey zza;
    public static final Api zzb;
    public static final zzz zzc;
    private static final Api.AbstractClientBuilder zzd;

    static {
        Api.ClientKey clientKey = new Api.ClientKey();
        zza = clientKey;
        zzd zzdVar = new zzd();
        zzd = zzdVar;
        zzb = new Api("AppDataSearch.LIGHTWEIGHT_API", zzdVar, clientKey);
        zzc = new zzal();
    }
}
