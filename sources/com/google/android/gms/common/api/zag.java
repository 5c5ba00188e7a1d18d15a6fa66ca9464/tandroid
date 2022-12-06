package com.google.android.gms.common.api;

import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.internal.BasePendingResult;
/* compiled from: com.google.android.gms:play-services-base@@18.1.0 */
/* loaded from: classes.dex */
final class zag<R extends Result> extends BasePendingResult<R> {
    private final Result zae;

    public zag(GoogleApiClient googleApiClient, Result result) {
        super(googleApiClient);
        this.zae = result;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.common.api.internal.BasePendingResult
    public final R createFailedResult(Status status) {
        return (R) this.zae;
    }
}
