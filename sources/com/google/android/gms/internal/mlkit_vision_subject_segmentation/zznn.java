package com.google.android.gms.internal.mlkit_vision_subject_segmentation;
/* loaded from: classes.dex */
public final class zznn {
    private Long zza;
    private zzoa zzb;
    private Boolean zzc;

    public final zznn zza(Long l) {
        this.zza = Long.valueOf(l.longValue() & Long.MAX_VALUE);
        return this;
    }

    public final zznn zzb(zzoa zzoaVar) {
        this.zzb = zzoaVar;
        return this;
    }

    public final zznn zzc(Boolean bool) {
        this.zzc = bool;
        return this;
    }

    public final zznp zzd() {
        return new zznp(this, null);
    }
}
