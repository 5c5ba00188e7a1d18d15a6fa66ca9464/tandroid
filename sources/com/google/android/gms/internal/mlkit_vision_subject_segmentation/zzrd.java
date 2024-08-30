package com.google.android.gms.internal.mlkit_vision_subject_segmentation;
/* loaded from: classes.dex */
public final class zzrd {
    private zzoa zza;
    private Long zzb;
    private zzrz zzc;

    public final zzrd zzb(Long l) {
        this.zzb = Long.valueOf(l.longValue() & Long.MAX_VALUE);
        return this;
    }

    public final zzrd zzc(zzoa zzoaVar) {
        this.zza = zzoaVar;
        return this;
    }

    public final zzrd zzd(zzrz zzrzVar) {
        this.zzc = zzrzVar;
        return this;
    }

    public final zzrf zze() {
        return new zzrf(this, null);
    }
}
