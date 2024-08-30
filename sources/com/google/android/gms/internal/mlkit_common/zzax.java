package com.google.android.gms.internal.mlkit_common;

import java.util.AbstractMap;
/* loaded from: classes.dex */
final class zzax extends zzaq {
    final /* synthetic */ zzay zza;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzax(zzay zzayVar) {
        this.zza = zzayVar;
    }

    @Override // java.util.List
    public final /* bridge */ /* synthetic */ Object get(int i) {
        int i2;
        Object[] objArr;
        Object[] objArr2;
        i2 = this.zza.zzc;
        zzae.zza(i, i2, "index");
        zzay zzayVar = this.zza;
        objArr = zzayVar.zzb;
        int i3 = i + i;
        Object obj = objArr[i3];
        obj.getClass();
        objArr2 = zzayVar.zzb;
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
