package com.google.android.gms.internal.mlkit_vision_label;

import java.util.Iterator;

/* loaded from: classes.dex */
final class zzbx extends zzbh {
    private final transient zzbg zza;
    private final transient zzbe zzb;

    zzbx(zzbg zzbgVar, zzbe zzbeVar) {
        this.zza = zzbgVar;
        this.zzb = zzbeVar;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_label.zzaz, java.util.AbstractCollection, java.util.Collection
    public final boolean contains(Object obj) {
        return this.zza.get(obj) != null;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public final /* synthetic */ Iterator iterator() {
        return this.zzb.listIterator(0);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final int size() {
        return this.zza.size();
    }

    @Override // com.google.android.gms.internal.mlkit_vision_label.zzaz
    final int zza(Object[] objArr, int i) {
        return this.zzb.zza(objArr, 0);
    }
}
