package com.google.android.gms.internal.mlkit_vision_label;

import java.util.AbstractMap;

/* loaded from: classes.dex */
final class zzbv extends zzbe {
    final /* synthetic */ zzbw zza;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzbv(zzbw zzbwVar) {
        this.zza = zzbwVar;
    }

    @Override // java.util.List
    public final /* bridge */ /* synthetic */ Object get(int i) {
        int i2;
        Object[] objArr;
        Object[] objArr2;
        i2 = this.zza.zzc;
        zzs.zza(i, i2, "index");
        zzbw zzbwVar = this.zza;
        int i3 = i + i;
        objArr = zzbwVar.zzb;
        Object obj = objArr[i3];
        obj.getClass();
        objArr2 = zzbwVar.zzb;
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
