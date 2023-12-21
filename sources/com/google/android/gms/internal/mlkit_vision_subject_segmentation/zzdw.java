package com.google.android.gms.internal.mlkit_vision_subject_segmentation;

import com.google.android.gms.common.internal.Objects;
/* compiled from: com.google.android.gms:play-services-mlkit-subject-segmentation@@16.0.0-beta1 */
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
        if (obj instanceof zzdw) {
            zzdw zzdwVar = (zzdw) obj;
            return Objects.equal(this.zza, zzdwVar.zza) && Objects.equal(this.zzb, zzdwVar.zzb) && Objects.equal(null, null) && Objects.equal(this.zzd, zzdwVar.zzd);
        }
        return false;
    }

    public final int hashCode() {
        return Objects.hashCode(this.zza, this.zzb, null, this.zzd);
    }

    @zzcd(zza = 1)
    public final zzoa zza() {
        return this.zza;
    }

    @zzcd(zza = 4)
    public final zzrz zzb() {
        return this.zzd;
    }

    @zzcd(zza = 2)
    public final Boolean zzc() {
        return this.zzb;
    }
}
