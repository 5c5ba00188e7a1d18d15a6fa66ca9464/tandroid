package com.google.android.gms.internal.mlkit_vision_label;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;
import java.util.Set;

/* loaded from: classes.dex */
abstract class zzag extends zzai implements Serializable {
    private transient Map zza;
    private transient int zzb;

    protected zzag(Map map) {
        if (!map.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.zza = map;
    }

    static /* synthetic */ int zzd(zzag zzagVar) {
        int i = zzagVar.zzb;
        zzagVar.zzb = i + 1;
        return i;
    }

    static /* synthetic */ int zze(zzag zzagVar) {
        int i = zzagVar.zzb;
        zzagVar.zzb = i - 1;
        return i;
    }

    static /* synthetic */ int zzf(zzag zzagVar, int i) {
        int i2 = zzagVar.zzb + i;
        zzagVar.zzb = i2;
        return i2;
    }

    static /* synthetic */ int zzg(zzag zzagVar, int i) {
        int i2 = zzagVar.zzb - i;
        zzagVar.zzb = i2;
        return i2;
    }

    static /* synthetic */ void zzm(zzag zzagVar, Object obj) {
        Object obj2;
        Map map = zzagVar.zza;
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
            zzagVar.zzb -= size;
        }
    }

    abstract Collection zza();

    abstract Collection zzb(Object obj, Collection collection);

    public final Collection zzh(Object obj) {
        Collection collection = (Collection) this.zza.get(obj);
        if (collection == null) {
            collection = zza();
        }
        return zzb(obj, collection);
    }

    final List zzi(Object obj, List list, zzad zzadVar) {
        return list instanceof RandomAccess ? new zzab(this, obj, list, zzadVar) : new zzaf(this, obj, list, zzadVar);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_label.zzai
    final Map zzk() {
        return new zzy(this, this.zza);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_label.zzai
    final Set zzl() {
        return new zzaa(this, this.zza);
    }

    public final void zzn() {
        Iterator it = this.zza.values().iterator();
        while (it.hasNext()) {
            ((Collection) it.next()).clear();
        }
        this.zza.clear();
        this.zzb = 0;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_label.zzbr
    public final boolean zzo(Object obj, Object obj2) {
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
