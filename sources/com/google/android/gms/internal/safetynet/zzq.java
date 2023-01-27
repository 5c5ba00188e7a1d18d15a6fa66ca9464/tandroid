package com.google.android.gms.internal.safetynet;

import com.google.android.gms.common.api.Status;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-safetynet@@17.0.1 */
/* loaded from: classes.dex */
public final class zzq extends zzd {
    final /* synthetic */ zzr zza;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzq(zzr zzrVar) {
        this.zza = zzrVar;
    }

    @Override // com.google.android.gms.internal.safetynet.zzg
    public final void zzd(Status status, com.google.android.gms.safetynet.zza zzaVar) {
        this.zza.setResult((zzr) new zzp(status, zzaVar));
    }
}
