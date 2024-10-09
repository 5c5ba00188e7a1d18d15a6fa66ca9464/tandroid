package com.google.android.gms.internal.mlkit_vision_subject_segmentation;

import com.google.android.gms.common.internal.Objects;

/* loaded from: classes.dex */
public final class zznj {
    private final zznh zza;
    private final Integer zzb;
    private final Integer zzc;
    private final Boolean zzd;

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ zznj(zzng zzngVar, zzni zzniVar) {
        zznh zznhVar;
        Integer num;
        zznhVar = zzngVar.zza;
        this.zza = zznhVar;
        num = zzngVar.zzb;
        this.zzb = num;
        this.zzc = null;
        this.zzd = null;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zznj)) {
            return false;
        }
        zznj zznjVar = (zznj) obj;
        return Objects.equal(this.zza, zznjVar.zza) && Objects.equal(this.zzb, zznjVar.zzb) && Objects.equal(null, null) && Objects.equal(null, null);
    }

    public final int hashCode() {
        return Objects.hashCode(this.zza, this.zzb, null, null);
    }

    public final zznh zza() {
        return this.zza;
    }

    public final Integer zzb() {
        return this.zzb;
    }
}
