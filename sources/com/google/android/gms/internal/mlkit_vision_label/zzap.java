package com.google.android.gms.internal.mlkit_vision_label;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
/* loaded from: classes.dex */
final class zzap extends AbstractSet {
    final /* synthetic */ zzau zza;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzap(zzau zzauVar) {
        this.zza = zzauVar;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final void clear() {
        this.zza.clear();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final boolean contains(Object obj) {
        int zzv;
        Map zzl = this.zza.zzl();
        if (zzl != null) {
            return zzl.entrySet().contains(obj);
        }
        if (obj instanceof Map.Entry) {
            Map.Entry entry = (Map.Entry) obj;
            zzv = this.zza.zzv(entry.getKey());
            if (zzv != -1 && zzo.zza(zzau.zzj(this.zza, zzv), entry.getValue())) {
                return true;
            }
        }
        return false;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public final Iterator iterator() {
        zzau zzauVar = this.zza;
        Map zzl = zzauVar.zzl();
        return zzl != null ? zzl.entrySet().iterator() : new zzan(zzauVar);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final boolean remove(Object obj) {
        int zzu;
        int[] zzz;
        Object[] zzA;
        Object[] zzB;
        Map zzl = this.zza.zzl();
        if (zzl != null) {
            return zzl.entrySet().remove(obj);
        }
        if (obj instanceof Map.Entry) {
            Map.Entry entry = (Map.Entry) obj;
            zzau zzauVar = this.zza;
            if (zzauVar.zzq()) {
                return false;
            }
            zzu = zzauVar.zzu();
            Object key = entry.getKey();
            Object value = entry.getValue();
            Object zzk = zzau.zzk(this.zza);
            zzz = this.zza.zzz();
            zzA = this.zza.zzA();
            zzB = this.zza.zzB();
            int zzb = zzav.zzb(key, value, zzu, zzk, zzz, zzA, zzB);
            if (zzb == -1) {
                return false;
            }
            this.zza.zzp(zzb, zzu);
            zzau.zzb(this.zza);
            this.zza.zzn();
            return true;
        }
        return false;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public final int size() {
        return this.zza.size();
    }
}
