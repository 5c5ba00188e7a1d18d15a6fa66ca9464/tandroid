package com.google.android.gms.internal.mlkit_vision_subject_segmentation;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;
import java.util.Set;

/* loaded from: classes.dex */
abstract class zzw extends zzy implements Serializable {
    private transient Map zza;
    private transient int zzb;

    protected zzw(Map map) {
        if (!map.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.zza = map;
    }

    static /* bridge */ /* synthetic */ void zzk(zzw zzwVar, Object obj) {
        Object obj2;
        Map map = zzwVar.zza;
        map.getClass();
        try {
            obj2 = map.remove(obj);
        } catch (ClassCastException | NullPointerException unused) {
            obj2 = null;
        }
        Collection collection = (Collection) obj2;
        if (collection != null) {
            int size = collection.size();
            collection.clear();
            zzwVar.zzb -= size;
        }
    }

    abstract Collection zza();

    abstract Collection zzb(Object obj, Collection collection);

    public final Collection zze(Object obj) {
        Collection collection = (Collection) this.zza.get(obj);
        if (collection == null) {
            collection = zza();
        }
        return zzb(obj, collection);
    }

    final List zzf(Object obj, List list, zzt zztVar) {
        return list instanceof RandomAccess ? new zzr(this, obj, list, zztVar) : new zzv(this, obj, list, zztVar);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzy
    final Map zzh() {
        return new zzo(this, this.zza);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzy
    final Set zzi() {
        return new zzq(this, this.zza);
    }

    public final void zzl() {
        Iterator it = this.zza.values().iterator();
        while (it.hasNext()) {
            ((Collection) it.next()).clear();
        }
        this.zza.clear();
        this.zzb = 0;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzbi
    public final boolean zzm(Object obj, Object obj2) {
        Collection collection = (Collection) this.zza.get(obj);
        if (collection != null) {
            if (!collection.add(obj2)) {
                return false;
            }
            this.zzb++;
            return true;
        }
        Collection zza = zza();
        if (!zza.add(obj2)) {
            throw new AssertionError("New Collection violated the Collection spec");
        }
        this.zzb++;
        this.zza.put(obj, zza);
        return true;
    }
}
