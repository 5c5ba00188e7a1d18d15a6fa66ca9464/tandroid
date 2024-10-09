package com.google.android.gms.internal.safetynet;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.safetynet.SafetyNetApi;

/* loaded from: classes.dex */
public final class zzae implements SafetyNetApi {
    public static PendingResult zza(GoogleApiClient googleApiClient, byte[] bArr, String str) {
        return googleApiClient.enqueue(new zzi(googleApiClient, bArr, str));
    }
}
