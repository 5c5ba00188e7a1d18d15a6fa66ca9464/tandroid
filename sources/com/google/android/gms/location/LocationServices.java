package com.google.android.gms.location;

import android.content.Context;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.internal.location.zzau;
import com.google.android.gms.internal.location.zzbp;
import com.google.android.gms.internal.location.zzbv;
import com.google.android.gms.internal.location.zzcc;
import com.google.android.gms.internal.location.zzce;
/* compiled from: com.google.android.gms:play-services-location@@21.0.1 */
/* loaded from: classes.dex */
public class LocationServices {
    @Deprecated
    public static final Api<Api.ApiOptions.NoOptions> API = zzbp.zzb;

    static {
        new zzau();
        new zzbv();
        new zzcc();
    }

    public static FusedLocationProviderClient getFusedLocationProviderClient(Context context) {
        return new zzbp(context);
    }

    public static SettingsClient getSettingsClient(Context context) {
        return new zzce(context);
    }
}
