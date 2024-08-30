package com.google.android.gms.internal.mlkit_vision_label;
/* loaded from: classes.dex */
public final class zzjv {
    private Long zza;
    private zzke zzb;
    private Boolean zzc;
    private Boolean zzd;
    private Boolean zze;

    public final zzjv zza(Boolean bool) {
        this.zzd = bool;
        return this;
    }

    public final zzjv zzb(Boolean bool) {
        this.zze = bool;
        return this;
    }

    public final zzjv zzc(Long l) {
        this.zza = Long.valueOf(l.longValue() & Long.MAX_VALUE);
        return this;
    }

    public final zzjv zzd(zzke zzkeVar) {
        this.zzb = zzkeVar;
        return this;
    }

    public final zzjv zze(Boolean bool) {
        this.zzc = bool;
        return this;
    }

    public final zzjx zzf() {
        return new zzjx(this, null);
    }
}
