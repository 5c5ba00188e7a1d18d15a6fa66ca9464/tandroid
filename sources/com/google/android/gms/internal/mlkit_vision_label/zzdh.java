package com.google.android.gms.internal.mlkit_vision_label;

import com.google.android.gms.common.internal.Objects;

/* loaded from: classes.dex */
public final class zzdh {
    private final zzke zza;
    private final Boolean zzb;
    private final zzjt zzc;
    private final zzlg zzd;

    /* synthetic */ zzdh(zzdf zzdfVar, zzdg zzdgVar) {
        zzke zzkeVar;
        Boolean bool;
        zzlg zzlgVar;
        zzkeVar = zzdfVar.zza;
        this.zza = zzkeVar;
        bool = zzdfVar.zzb;
        this.zzb = bool;
        this.zzc = null;
        zzlgVar = zzdfVar.zzc;
        this.zzd = zzlgVar;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzdh)) {
            return false;
        }
        zzdh zzdhVar = (zzdh) obj;
        return Objects.equal(this.zza, zzdhVar.zza) && Objects.equal(this.zzb, zzdhVar.zzb) && Objects.equal(null, null) && Objects.equal(this.zzd, zzdhVar.zzd);
    }

    public final int hashCode() {
        return Objects.hashCode(this.zza, this.zzb, null, this.zzd);
    }

    public final zzke zza() {
        return this.zza;
    }

    public final zzlg zzb() {
        return this.zzd;
    }

    public final Boolean zzc() {
        return this.zzb;
    }
}
