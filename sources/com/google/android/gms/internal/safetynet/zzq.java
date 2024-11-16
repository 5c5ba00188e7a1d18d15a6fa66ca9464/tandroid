package com.google.android.gms.internal.safetynet;

import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;

/* loaded from: classes.dex */
final class zzq extends zzd {
    final /* synthetic */ zzr zza;

    zzq(zzr zzrVar) {
        this.zza = zzrVar;
    }

    @Override // com.google.android.gms.internal.safetynet.zzg
    public final void zzd(Status status, com.google.android.gms.safetynet.zza zzaVar) {
        this.zza.setResult((Result) new zzp(status, zzaVar));
    }
}
