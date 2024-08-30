package com.google.android.gms.location;

import android.content.Context;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.internal.location.zzau;
import com.google.android.gms.internal.location.zzbp;
import com.google.android.gms.internal.location.zzbv;
import com.google.android.gms.internal.location.zzcc;
import com.google.android.gms.internal.location.zzce;
/* loaded from: classes.dex */
public abstract class LocationServices {
    public static final Api API = zzbp.zzb;
    public static final FusedLocationProviderApi FusedLocationApi = new zzau();
    public static final GeofencingApi GeofencingApi = new zzbv();
    public static final SettingsApi SettingsApi = new zzcc();

    public static FusedLocationProviderClient getFusedLocationProviderClient(Context context) {
        return new zzbp(context);
    }

    public static SettingsClient getSettingsClient(Context context) {
        return new zzce(context);
    }
}
