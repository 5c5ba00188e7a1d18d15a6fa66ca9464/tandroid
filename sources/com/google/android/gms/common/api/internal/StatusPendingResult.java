package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
/* compiled from: com.google.android.gms:play-services-base@@18.1.0 */
/* loaded from: classes.dex */
public class StatusPendingResult extends BasePendingResult<Status> {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.common.api.internal.BasePendingResult
    public final /* bridge */ /* synthetic */ Status createFailedResult(Status status) {
        return status;
    }

    public StatusPendingResult(GoogleApiClient googleApiClient) {
        super(googleApiClient);
    }
}
