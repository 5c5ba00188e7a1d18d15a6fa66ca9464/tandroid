package com.google.android.gms.safetynet;

import android.content.Context;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.internal.safetynet.zzae;
import com.google.android.gms.internal.safetynet.zzaf;
import com.google.android.gms.internal.safetynet.zzag;
/* compiled from: com.google.android.gms:play-services-safetynet@@17.0.1 */
/* loaded from: classes.dex */
public final class SafetyNet {
    @Deprecated
    public static final Api<Api.ApiOptions.NoOptions> API;
    public static final Api.ClientKey<zzaf> zza;
    public static final Api.AbstractClientBuilder<zzaf, Api.ApiOptions.NoOptions> zzb;

    static {
        Api.ClientKey<zzaf> clientKey = new Api.ClientKey<>();
        zza = clientKey;
        zzk zzkVar = new zzk();
        zzb = zzkVar;
        API = new Api<>("SafetyNet.API", zzkVar, clientKey);
        new zzae();
        new zzag();
    }

    public static SafetyNetClient getClient(Context context) {
        return new SafetyNetClient(context);
    }
}
