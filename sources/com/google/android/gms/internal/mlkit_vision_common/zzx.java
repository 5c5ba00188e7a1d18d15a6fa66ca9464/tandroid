package com.google.android.gms.internal.mlkit_vision_common;

import java.util.Iterator;

/* loaded from: classes.dex */
final class zzx extends zzs {
    private final transient zzr zza;
    private final transient zzp zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzx(zzr zzrVar, zzp zzpVar) {
        this.zza = zzrVar;
        this.zzb = zzpVar;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_common.zzl, java.util.AbstractCollection, java.util.Collection
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

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.mlkit_vision_common.zzl
    public final int zza(Object[] objArr, int i) {
        return this.zzb.zza(objArr, 0);
    }
}
