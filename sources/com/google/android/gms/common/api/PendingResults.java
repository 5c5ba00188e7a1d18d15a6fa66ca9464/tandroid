package com.google.android.gms.common.api;

import com.google.android.gms.common.api.internal.StatusPendingResult;
import com.google.android.gms.common.internal.Preconditions;
/* loaded from: classes.dex */
public abstract class PendingResults {
    public static PendingResult immediateFailedResult(Result result, GoogleApiClient googleApiClient) {
        Preconditions.checkNotNull(result, "Result must not be null");
        Preconditions.checkArgument(!result.getStatus().isSuccess(), "Status code must not be SUCCESS");
        zag zagVar = new zag(googleApiClient, result);
        zagVar.setResult(result);
        return zagVar;
    }

    public static PendingResult immediatePendingResult(Status status, GoogleApiClient googleApiClient) {
        Preconditions.checkNotNull(status, "Result must not be null");
        StatusPendingResult statusPendingResult = new StatusPendingResult(googleApiClient);
        statusPendingResult.setResult(status);
        return statusPendingResult;
    }
}
