package com.google.android.gms.safetynet;

import android.content.Context;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.internal.safetynet.zzae;
import com.google.android.gms.internal.safetynet.zzag;
/* loaded from: classes.dex */
public abstract class SafetyNet {
    public static final Api API;
    public static final SafetyNetApi SafetyNetApi;
    public static final Api.ClientKey zza;
    public static final Api.AbstractClientBuilder zzb;
    public static final zzag zzc;

    static {
        Api.ClientKey clientKey = new Api.ClientKey();
        zza = clientKey;
        zzk zzkVar = new zzk();
        zzb = zzkVar;
        API = new Api("SafetyNet.API", zzkVar, clientKey);
        SafetyNetApi = new zzae();
        zzc = new zzag();
    }

    public static SafetyNetClient getClient(Context context) {
        return new SafetyNetClient(context);
    }
}
