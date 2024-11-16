package com.google.android.gms.internal.mlkit_vision_subject_segmentation;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;

/* loaded from: classes.dex */
abstract class zzt extends AbstractCollection {
    final Object zza;
    Collection zzb;
    final zzt zzc;
    final Collection zzd;
    final /* synthetic */ zzw zze;

    zzt(zzw zzwVar, Object obj, Collection collection, zzt zztVar) {
        this.zze = zzwVar;
        this.zza = obj;
        this.zzb = collection;
        this.zzc = zztVar;
        this.zzd = zztVar == null ? null : zztVar.zzb;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public final boolean add(Object obj) {
        int i;
        zzb();
        boolean isEmpty = this.zzb.isEmpty();
        boolean add = this.zzb.add(obj);
        if (add) {
            zzw zzwVar = this.zze;
            i = zzwVar.zzb;
            zzwVar.zzb = i + 1;
            if (isEmpty) {
                zza();
                return true;
            }
        }
        return add;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public final boolean addAll(Collection collection) {
        int i;
        if (collection.isEmpty()) {
            return false;
        }
        int size = size();
        boolean addAll = this.zzb.addAll(collection);
        if (!addAll) {
            return addAll;
        }
        int size2 = this.zzb.size();
        zzw zzwVar = this.zze;
        i = zzwVar.zzb;
        zzwVar.zzb = i + (size2 - size);
        if (size != 0) {
            return addAll;
        }
        zza();
        return true;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public final void clear() {
        int i;
        int size = size();
        if (size == 0) {
            return;
        }
        this.zzb.clear();
        zzw zzwVar = this.zze;
        i = zzwVar.zzb;
        zzwVar.zzb = i - size;
        zzc();
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public final boolean contains(Object obj) {
        zzb();
        return this.zzb.contains(obj);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public final boolean containsAll(Collection collection) {
        zzb();
        return this.zzb.containsAll(collection);
    }

    @Override // java.util.Collection
    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        zzb();
        return this.zzb.equals(obj);
    }

    @Override // java.util.Collection
    public final int hashCode() {
        zzb();
        return this.zzb.hashCode();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
    public final Iterator iterator() {
        zzb();
        return new zzs(this);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public final boolean remove(Object obj) {
        int i;
        zzb();
        boolean remove = this.zzb.remove(obj);
        if (remove) {
            zzw zzwVar = this.zze;
            i = zzwVar.zzb;
            zzwVar.zzb = i - 1;
            zzc();
        }
        return remove;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public final boolean removeAll(Collection collection) {
        int i;
        if (collection.isEmpty()) {
            return false;
        }
        int size = size();
        boolean removeAll = this.zzb.removeAll(collection);
        if (removeAll) {
            int size2 = this.zzb.size();
            zzw zzwVar = this.zze;
            int i2 = size2 - size;
            i = zzwVar.zzb;
            zzwVar.zzb = i + i2;
            zzc();
        }
        return removeAll;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public final boolean retainAll(Collection collection) {
        int i;
        collection.getClass();
        int size = size();
        boolean retainAll = this.zzb.retainAll(collection);
        if (retainAll) {
            int size2 = this.zzb.size();
            zzw zzwVar = this.zze;
            int i2 = size2 - size;
            i = zzwVar.zzb;
            zzwVar.zzb = i + i2;
            zzc();
        }
        return retainAll;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public final int size() {
        zzb();
        return this.zzb.size();
    }

    @Override // java.util.AbstractCollection
    public final String toString() {
        zzb();
        return this.zzb.toString();
    }

    final void zza() {
        Map map;
        zzt zztVar = this.zzc;
        if (zztVar != null) {
            zztVar.zza();
            return;
        }
        zzw zzwVar = this.zze;
        Object obj = this.zza;
        map = zzwVar.zza;
        map.put(obj, this.zzb);
    }

    final void zzb() {
        Map map;
        zzt zztVar = this.zzc;
        if (zztVar != null) {
            zztVar.zzb();
            zzt zztVar2 = this.zzc;
            if (zztVar2.zzb != this.zzd) {
                throw new ConcurrentModificationException();
            }
            return;
        }
        if (this.zzb.isEmpty()) {
            zzw zzwVar = this.zze;
            Object obj = this.zza;
            map = zzwVar.zza;
            Collection collection = (Collection) map.get(obj);
            if (collection != null) {
                this.zzb = collection;
            }
        }
    }

    final void zzc() {
        Map map;
        zzt zztVar = this.zzc;
        if (zztVar != null) {
            zztVar.zzc();
        } else if (this.zzb.isEmpty()) {
            zzw zzwVar = this.zze;
            Object obj = this.zza;
            map = zzwVar.zza;
            map.remove(obj);
        }
    }
}
