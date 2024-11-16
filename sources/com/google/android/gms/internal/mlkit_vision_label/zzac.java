package com.google.android.gms.internal.mlkit_vision_label;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
class zzac implements Iterator {
    final Iterator zza;
    final Collection zzb;
    final /* synthetic */ zzad zzc;

    zzac(zzad zzadVar) {
        this.zzc = zzadVar;
        Collection collection = zzadVar.zzb;
        this.zzb = collection;
        this.zza = collection instanceof List ? ((List) collection).listIterator() : collection.iterator();
    }

    zzac(zzad zzadVar, Iterator it) {
        this.zzc = zzadVar;
        this.zzb = zzadVar.zzb;
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
        this.zza.remove();
        zzag.zze(this.zzc.zze);
        this.zzc.zzc();
    }

    final void zza() {
        this.zzc.zzb();
        if (this.zzc.zzb != this.zzb) {
            throw new ConcurrentModificationException();
        }
    }
}
