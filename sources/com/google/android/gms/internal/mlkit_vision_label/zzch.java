package com.google.android.gms.internal.mlkit_vision_label;

/* loaded from: classes.dex */
final class zzch implements zzcm {
    private final int zza;
    private final zzcl zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzch(int i, zzcl zzclVar) {
        this.zza = i;
        this.zzb = zzclVar;
    }

    @Override // java.lang.annotation.Annotation
    public final Class annotationType() {
        return zzcm.class;
    }

    @Override // java.lang.annotation.Annotation
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof zzcm)) {
            return false;
        }
        zzcm zzcmVar = (zzcm) obj;
        return this.zza == zzcmVar.zza() && this.zzb.equals(zzcmVar.zzb());
    }

    @Override // java.lang.annotation.Annotation
    public final int hashCode() {
        return (this.zza ^ 14552422) + (this.zzb.hashCode() ^ 2041407134);
    }

    @Override // java.lang.annotation.Annotation
    public final String toString() {
        return "@com.google.firebase.encoders.proto.Protobuf(tag=" + this.zza + "intEncoding=" + this.zzb + ')';
    }

    @Override // com.google.android.gms.internal.mlkit_vision_label.zzcm
    public final int zza() {
        return this.zza;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_label.zzcm
    public final zzcl zzb() {
        return this.zzb;
    }
}
