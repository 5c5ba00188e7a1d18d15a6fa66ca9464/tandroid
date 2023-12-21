package com.google.android.gms.internal.mlkit_vision_subject_segmentation;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.Set;
/* compiled from: com.google.android.gms:play-services-mlkit-subject-segmentation@@16.0.0-beta1 */
/* loaded from: classes.dex */
public abstract class zzw extends zzy implements Serializable {
    private transient Map zza;
    private transient int zzb;

    public zzw(Map map) {
        if (map.isEmpty()) {
            this.zza = map;
            return;
        }
        throw new IllegalArgumentException();
    }

    public static /* bridge */ /* synthetic */ int zzd(zzw zzwVar) {
        return zzwVar.zzb;
    }

    public static /* bridge */ /* synthetic */ Map zzg(zzw zzwVar) {
        return zzwVar.zza;
    }

    public static /* bridge */ /* synthetic */ void zzj(zzw zzwVar, int i) {
        zzwVar.zzb = i;
    }

    public static /* bridge */ /* synthetic */ void zzk(zzw zzwVar, Object obj) {
        Object obj2;
        Map map = zzwVar.zza;
        Objects.requireNonNull(map);
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

    public abstract Collection zza();

    public abstract Collection zzb(Object obj, Collection collection);

    public final Collection zze(Object obj) {
        Collection collection = (Collection) this.zza.get(obj);
        if (collection == null) {
            collection = zza();
        }
        return zzb(obj, collection);
    }

    public final List zzf(Object obj, List list, zzt zztVar) {
        if (list instanceof RandomAccess) {
            return new zzr(this, obj, list, zztVar);
        }
        return new zzv(this, obj, list, zztVar);
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
        for (Collection collection : this.zza.values()) {
            collection.clear();
        }
        this.zza.clear();
        this.zzb = 0;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzbi
    public final boolean zzm(Object obj, Object obj2) {
        Collection collection = (Collection) this.zza.get(obj);
        if (collection == null) {
            Collection zza = zza();
            if (zza.add(obj2)) {
                this.zzb++;
                this.zza.put(obj, zza);
                return true;
            }
            throw new AssertionError("New Collection violated the Collection spec");
        } else if (collection.add(obj2)) {
            this.zzb++;
            return true;
        } else {
            return false;
        }
    }
}
