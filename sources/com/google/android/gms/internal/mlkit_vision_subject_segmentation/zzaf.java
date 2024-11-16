package com.google.android.gms.internal.mlkit_vision_subject_segmentation;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;

/* loaded from: classes.dex */
final class zzaf extends AbstractSet {
    final /* synthetic */ zzal zza;

    zzaf(zzal zzalVar) {
        this.zza = zzalVar;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final void clear() {
        this.zza.clear();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final boolean contains(Object obj) {
        int zzq;
        Map zzj = this.zza.zzj();
        if (zzj != null) {
            return zzj.entrySet().contains(obj);
        }
        if (obj instanceof Map.Entry) {
            Map.Entry entry = (Map.Entry) obj;
            zzq = this.zza.zzq(entry.getKey());
            if (zzq != -1) {
                Object[] objArr = this.zza.zzc;
                objArr.getClass();
                if (zze.zza(objArr[zzq], entry.getValue())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public final Iterator iterator() {
        zzal zzalVar = this.zza;
        Map zzj = zzalVar.zzj();
        return zzj != null ? zzj.entrySet().iterator() : new zzad(zzalVar);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final boolean remove(Object obj) {
        int zzp;
        int i;
        Map zzj = this.zza.zzj();
        if (zzj != null) {
            return zzj.entrySet().remove(obj);
        }
        if (!(obj instanceof Map.Entry)) {
            return false;
        }
        Map.Entry entry = (Map.Entry) obj;
        zzal zzalVar = this.zza;
        if (zzalVar.zzo()) {
            return false;
        }
        zzp = zzalVar.zzp();
        Object key = entry.getKey();
        Object value = entry.getValue();
        zzal zzalVar2 = this.zza;
        Object zzh = zzal.zzh(zzalVar2);
        int[] iArr = zzalVar2.zza;
        iArr.getClass();
        zzal zzalVar3 = this.zza;
        Object[] objArr = zzalVar3.zzb;
        objArr.getClass();
        Object[] objArr2 = zzalVar3.zzc;
        objArr2.getClass();
        int zzb = zzam.zzb(key, value, zzp, zzh, iArr, objArr, objArr2);
        if (zzb == -1) {
            return false;
        }
        this.zza.zzn(zzb, zzp);
        zzal zzalVar4 = this.zza;
        i = zzalVar4.zzg;
        zzalVar4.zzg = i - 1;
        this.zza.zzl();
        return true;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final int size() {
        return this.zza.size();
    }
}
