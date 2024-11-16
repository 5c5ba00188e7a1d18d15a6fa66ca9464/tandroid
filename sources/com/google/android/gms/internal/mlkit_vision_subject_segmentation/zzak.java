package com.google.android.gms.internal.mlkit_vision_subject_segmentation;

import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.Map;

/* loaded from: classes.dex */
final class zzak extends AbstractCollection {
    final /* synthetic */ zzal zza;

    zzak(zzal zzalVar) {
        this.zza = zzalVar;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public final void clear() {
        this.zza.clear();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
    public final Iterator iterator() {
        zzal zzalVar = this.zza;
        Map zzj = zzalVar.zzj();
        return zzj != null ? zzj.values().iterator() : new zzae(zzalVar);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public final int size() {
        return this.zza.size();
    }
}
