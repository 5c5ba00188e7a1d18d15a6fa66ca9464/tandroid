package com.google.android.gms.internal.mlkit_vision_subject_segmentation;

/* loaded from: classes.dex */
final class zzsz extends zztd {
    private final String zza;
    private final boolean zzb;
    private final int zzc;

    /* synthetic */ zzsz(String str, boolean z, int i, zzsy zzsyVar) {
        this.zza = str;
        this.zzb = z;
        this.zzc = i;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof zztd) {
            zztd zztdVar = (zztd) obj;
            if (this.zza.equals(zztdVar.zzb()) && this.zzb == zztdVar.zzc() && this.zzc == zztdVar.zza()) {
                return true;
            }
        }
        return false;
    }

    public final int hashCode() {
        return ((((this.zza.hashCode() ^ 1000003) * 1000003) ^ (true != this.zzb ? 1237 : 1231)) * 1000003) ^ this.zzc;
    }

    public final String toString() {
        return "MLKitLoggingOptions{libraryName=" + this.zza + ", enableFirelog=" + this.zzb + ", firelogEventType=" + this.zzc + "}";
    }

    @Override // com.google.android.gms.internal.mlkit_vision_subject_segmentation.zztd
    public final int zza() {
        return this.zzc;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_subject_segmentation.zztd
    public final String zzb() {
        return this.zza;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_subject_segmentation.zztd
    public final boolean zzc() {
        return this.zzb;
    }
}
