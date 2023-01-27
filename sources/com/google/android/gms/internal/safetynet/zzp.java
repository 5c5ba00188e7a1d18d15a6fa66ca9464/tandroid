package com.google.android.gms.internal.safetynet;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.safetynet.SafetyNetApi$AttestationResult;
/* compiled from: com.google.android.gms:play-services-safetynet@@17.0.1 */
/* loaded from: classes.dex */
final class zzp implements SafetyNetApi$AttestationResult {
    private final Status zza;
    private final com.google.android.gms.safetynet.zza zzb;

    public zzp(Status status, com.google.android.gms.safetynet.zza zzaVar) {
        this.zza = status;
        this.zzb = zzaVar;
    }

    @Override // com.google.android.gms.safetynet.SafetyNetApi$AttestationResult
    public final String getJwsResult() {
        com.google.android.gms.safetynet.zza zzaVar = this.zzb;
        if (zzaVar == null) {
            return null;
        }
        return zzaVar.zza();
    }

    @Override // com.google.android.gms.common.api.Result
    public final Status getStatus() {
        return this.zza;
    }
}
