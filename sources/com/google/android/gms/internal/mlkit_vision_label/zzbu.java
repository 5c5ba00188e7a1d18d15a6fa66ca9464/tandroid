package com.google.android.gms.internal.mlkit_vision_label;

/* loaded from: classes.dex */
final class zzbu extends zzbe {
    static final zzbe zza = new zzbu(new Object[0], 0);
    final transient Object[] zzb;
    private final transient int zzc;

    zzbu(Object[] objArr, int i) {
        this.zzb = objArr;
        this.zzc = i;
    }

    @Override // java.util.List
    public final Object get(int i) {
        zzs.zza(i, this.zzc, "index");
        Object obj = this.zzb[i];
        obj.getClass();
        return obj;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final int size() {
        return this.zzc;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_label.zzbe, com.google.android.gms.internal.mlkit_vision_label.zzaz
    final int zza(Object[] objArr, int i) {
        System.arraycopy(this.zzb, 0, objArr, 0, this.zzc);
        return this.zzc;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_label.zzaz
    final int zzb() {
        return this.zzc;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_label.zzaz
    final int zzc() {
        return 0;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_label.zzaz
    final Object[] zze() {
        return this.zzb;
    }
}
