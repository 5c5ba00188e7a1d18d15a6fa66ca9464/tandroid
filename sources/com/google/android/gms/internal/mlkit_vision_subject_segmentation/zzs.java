package com.google.android.gms.internal.mlkit_vision_subject_segmentation;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
class zzs implements Iterator {
    final Iterator zza;
    final Collection zzb;
    final /* synthetic */ zzt zzc;

    zzs(zzt zztVar) {
        this.zzc = zztVar;
        Collection collection = zztVar.zzb;
        this.zzb = collection;
        this.zza = collection instanceof List ? ((List) collection).listIterator() : collection.iterator();
    }

    zzs(zzt zztVar, Iterator it) {
        this.zzc = zztVar;
        this.zzb = zztVar.zzb;
        this.zza = it;
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        zza();
        return this.zza.hasNext();
    }

    @Override // java.util.Iterator
    public final Object next() {
        zza();
        return this.zza.next();
    }

    @Override // java.util.Iterator
    public final void remove() {
        int i;
        this.zza.remove();
        zzw zzwVar = this.zzc.zze;
        i = zzwVar.zzb;
        zzwVar.zzb = i - 1;
        this.zzc.zzc();
    }

    final void zza() {
        this.zzc.zzb();
        if (this.zzc.zzb != this.zzb) {
            throw new ConcurrentModificationException();
        }
    }
}
