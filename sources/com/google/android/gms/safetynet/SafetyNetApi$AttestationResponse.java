package com.google.android.gms.safetynet;

import com.google.android.gms.common.api.Response;
/* compiled from: com.google.android.gms:play-services-safetynet@@17.0.1 */
/* loaded from: classes.dex */
public class SafetyNetApi$AttestationResponse extends Response<SafetyNetApi$AttestationResult> {
    public String getJwsResult() {
        return getResult().getJwsResult();
    }
}
