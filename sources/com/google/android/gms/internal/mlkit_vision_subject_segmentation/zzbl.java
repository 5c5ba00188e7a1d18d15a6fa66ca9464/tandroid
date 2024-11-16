package com.google.android.gms.internal.mlkit_vision_subject_segmentation;

/* loaded from: classes.dex */
final class zzbl extends zzav {
    static final zzav zza = new zzbl(new Object[0], 0);
    final transient Object[] zzb;
    private final transient int zzc;

    zzbl(Object[] objArr, int i) {
        this.zzb = objArr;
        this.zzc = i;
    }

    @Override // java.util.List
    public final Object get(int i) {
        zzi.zza(i, this.zzc, "index");
        Object obj = this.zzb[i];
        obj.getClass();
        return obj;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final int size() {
        return this.zzc;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzav, com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzaq
    final int zza(Object[] objArr, int i) {
        System.arraycopy(this.zzb, 0, objArr, 0, this.zzc);
        return this.zzc;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzaq
    final int zzb() {
        return this.zzc;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzaq
    final int zzc() {
        return 0;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzaq
    final Object[] zze() {
        return this.zzb;
    }
}
