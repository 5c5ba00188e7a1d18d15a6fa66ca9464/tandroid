package com.google.android.gms.safetynet;

import com.google.android.gms.common.api.Response;
import com.google.android.gms.common.api.Result;
/* compiled from: com.google.android.gms:play-services-safetynet@@17.0.1 */
/* loaded from: classes.dex */
public interface SafetyNetApi {

    /* compiled from: com.google.android.gms:play-services-safetynet@@17.0.1 */
    /* loaded from: classes.dex */
    public static class AttestationResponse extends Response<AttestationResult> {
        public String getJwsResult() {
            return getResult().getJwsResult();
        }
    }

    /* compiled from: com.google.android.gms:play-services-safetynet@@17.0.1 */
    @Deprecated
    /* loaded from: classes.dex */
    public interface AttestationResult extends Result {
        String getJwsResult();
    }
}
