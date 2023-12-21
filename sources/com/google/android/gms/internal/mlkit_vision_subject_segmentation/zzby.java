package com.google.android.gms.internal.mlkit_vision_subject_segmentation;
/* compiled from: com.google.android.gms:play-services-mlkit-subject-segmentation@@16.0.0-beta1 */
/* loaded from: classes.dex */
final class zzby implements zzcd {
    private final int zza;
    private final zzcc zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzby(int i, zzcc zzccVar) {
        this.zza = i;
        this.zzb = zzccVar;
    }

    @Override // java.lang.annotation.Annotation
    public final Class annotationType() {
        return zzcd.class;
    }

    @Override // java.lang.annotation.Annotation
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof zzcd) {
            zzcd zzcdVar = (zzcd) obj;
            return this.zza == zzcdVar.zza() && this.zzb.equals(zzcdVar.zzb());
        }
        return false;
    }

    @Override // java.lang.annotation.Annotation
    public final int hashCode() {
        return (this.zza ^ 14552422) + (this.zzb.hashCode() ^ 2041407134);
    }

    @Override // java.lang.annotation.Annotation
    public final String toString() {
        return "@com.google.firebase.encoders.proto.Protobuf(tag=" + this.zza + "intEncoding=" + this.zzb + ')';
    }

    @Override // com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzcd
    public final int zza() {
        return this.zza;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzcd
    public final zzcc zzb() {
        return this.zzb;
    }
}
