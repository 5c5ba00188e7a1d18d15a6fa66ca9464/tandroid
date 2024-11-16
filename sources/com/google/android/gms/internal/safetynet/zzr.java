package com.google.android.gms.internal.safetynet;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;

/* loaded from: classes.dex */
abstract class zzr extends zze {
    protected final zzg zzc;

    public zzr(GoogleApiClient googleApiClient) {
        super(googleApiClient);
        this.zzc = new zzq(this);
    }

    @Override // com.google.android.gms.common.api.internal.BasePendingResult
    protected final /* bridge */ /* synthetic */ Result createFailedResult(Status status) {
        return new zzp(status, null);
    }
}
