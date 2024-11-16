package com.google.android.gms.internal.mlkit_vision_subject_segmentation;

import java.util.AbstractMap;

/* loaded from: classes.dex */
final class zzbm extends zzav {
    final /* synthetic */ zzbn zza;

    zzbm(zzbn zzbnVar) {
        this.zza = zzbnVar;
    }

    @Override // java.util.List
    public final /* bridge */ /* synthetic */ Object get(int i) {
        int i2;
        Object[] objArr;
        Object[] objArr2;
        i2 = this.zza.zzc;
        zzi.zza(i, i2, "index");
        zzbn zzbnVar = this.zza;
        objArr = zzbnVar.zzb;
        int i3 = i + i;
        Object obj = objArr[i3];
        obj.getClass();
        objArr2 = zzbnVar.zzb;
        Object obj2 = objArr2[i3 + 1];
        obj2.getClass();
        return new AbstractMap.SimpleImmutableEntry(obj, obj2);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final int size() {
        int i;
        i = this.zza.zzc;
        return i;
    }
}
