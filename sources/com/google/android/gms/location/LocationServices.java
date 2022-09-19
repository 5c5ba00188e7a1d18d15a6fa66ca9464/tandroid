package com.google.android.gms.location;

import android.content.Context;
import androidx.annotation.RecentlyNonNull;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.internal.location.zzaf;
import com.google.android.gms.internal.location.zzbi;
import com.google.android.gms.internal.location.zzz;
/* compiled from: com.google.android.gms:play-services-location@@18.0.0 */
/* loaded from: classes.dex */
public class LocationServices {
    @RecentlyNonNull
    public static final Api<Api.ApiOptions.NoOptions> API;
    private static final Api.ClientKey<com.google.android.gms.internal.location.zzaz> zza;
    private static final Api.AbstractClientBuilder<com.google.android.gms.internal.location.zzaz, Api.ApiOptions.NoOptions> zzb;

    static {
        Api.ClientKey<com.google.android.gms.internal.location.zzaz> clientKey = new Api.ClientKey<>();
        zza = clientKey;
        zzbh zzbhVar = new zzbh();
        zzb = zzbhVar;
        API = new Api<>("LocationServices.API", zzbhVar, clientKey);
        new zzz();
        new zzaf();
        new zzbi();
    }

    @RecentlyNonNull
    public static FusedLocationProviderClient getFusedLocationProviderClient(@RecentlyNonNull Context context) {
        return new FusedLocationProviderClient(context);
    }
}
