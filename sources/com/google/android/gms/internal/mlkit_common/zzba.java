package com.google.android.gms.internal.mlkit_common;

/* loaded from: classes.dex */
final class zzba extends zzaq {
    private final transient Object[] zza;
    private final transient int zzb;
    private final transient int zzc;

    zzba(Object[] objArr, int i, int i2) {
        this.zza = objArr;
        this.zzb = i;
        this.zzc = i2;
    }

    @Override // java.util.List
    public final Object get(int i) {
        zzae.zza(i, this.zzc, "index");
        Object obj = this.zza[i + i + this.zzb];
        obj.getClass();
        return obj;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final int size() {
        return this.zzc;
    }
}
