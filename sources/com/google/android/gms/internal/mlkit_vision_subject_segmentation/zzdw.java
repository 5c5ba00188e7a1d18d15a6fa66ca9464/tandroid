package com.google.android.gms.internal.mlkit_vision_subject_segmentation;

import com.google.android.gms.common.internal.Objects;

/* loaded from: classes.dex */
public final class zzdw {
    private final zzoa zza;
    private final Boolean zzb;
    private final zznj zzc;
    private final zzrz zzd;

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ zzdw(zzdu zzduVar, zzdv zzdvVar) {
        zzoa zzoaVar;
        Boolean bool;
        zzrz zzrzVar;
        zzoaVar = zzduVar.zza;
        this.zza = zzoaVar;
        bool = zzduVar.zzb;
        this.zzb = bool;
        this.zzc = null;
        zzrzVar = zzduVar.zzc;
        this.zzd = zzrzVar;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzdw)) {
            return false;
        }
        zzdw zzdwVar = (zzdw) obj;
        return Objects.equal(this.zza, zzdwVar.zza) && Objects.equal(this.zzb, zzdwVar.zzb) && Objects.equal(null, null) && Objects.equal(this.zzd, zzdwVar.zzd);
    }

    public final int hashCode() {
        return Objects.hashCode(this.zza, this.zzb, null, this.zzd);
    }

    public final zzoa zza() {
        return this.zza;
    }

    public final zzrz zzb() {
        return this.zzd;
    }

    public final Boolean zzc() {
        return this.zzb;
    }
}
