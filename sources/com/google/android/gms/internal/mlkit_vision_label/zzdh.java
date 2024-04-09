package com.google.android.gms.internal.mlkit_vision_label;

import com.google.android.gms.common.internal.Objects;
/* compiled from: com.google.android.gms:play-services-mlkit-image-labeling@@16.0.8 */
/* loaded from: classes.dex */
public final class zzdh {
    private final zzke zza;
    private final Boolean zzb;
    private final zzjt zzc;
    private final zzlg zzd;

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ zzdh(zzdf zzdfVar, zzdg zzdgVar) {
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
        if (obj instanceof zzdh) {
            zzdh zzdhVar = (zzdh) obj;
            return Objects.equal(this.zza, zzdhVar.zza) && Objects.equal(this.zzb, zzdhVar.zzb) && Objects.equal(null, null) && Objects.equal(this.zzd, zzdhVar.zzd);
        }
        return false;
    }

    public final int hashCode() {
        return Objects.hashCode(this.zza, this.zzb, null, this.zzd);
    }

    @zzcm(zza = 1)
    public final zzke zza() {
        return this.zza;
    }

    @zzcm(zza = 4)
    public final zzlg zzb() {
        return this.zzd;
    }

    @zzcm(zza = 2)
    public final Boolean zzc() {
        return this.zzb;
    }
}
