package com.google.android.gms.internal.mlkit_vision_subject_segmentation;

import java.util.Iterator;

/* loaded from: classes.dex */
final class zzbo extends zzay {
    private final transient zzax zza;
    private final transient zzav zzb;

    zzbo(zzax zzaxVar, zzav zzavVar) {
        this.zza = zzaxVar;
        this.zzb = zzavVar;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzaq, java.util.AbstractCollection, java.util.Collection
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

    @Override // com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzaq
    final int zza(Object[] objArr, int i) {
        return this.zzb.zza(objArr, 0);
    }
}
