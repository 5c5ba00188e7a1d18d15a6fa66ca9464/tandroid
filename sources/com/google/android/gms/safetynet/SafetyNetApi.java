package com.google.android.gms.safetynet;

import com.google.android.gms.common.api.Response;
import com.google.android.gms.common.api.Result;
/* loaded from: classes.dex */
public interface SafetyNetApi {

    /* loaded from: classes.dex */
    public static class AttestationResponse extends Response {
        public String getJwsResult() {
            return ((AttestationResult) getResult()).getJwsResult();
        }
    }

    /* loaded from: classes.dex */
    public interface AttestationResult extends Result {
        String getJwsResult();
    }
}
