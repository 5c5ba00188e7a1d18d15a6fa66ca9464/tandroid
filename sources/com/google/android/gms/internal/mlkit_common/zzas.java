package com.google.android.gms.internal.mlkit_common;

import java.util.Arrays;
/* loaded from: classes.dex */
public final class zzas {
    Object[] zza = new Object[8];
    int zzb = 0;
    zzar zzc;

    public final zzas zza(Object obj, Object obj2) {
        int i = this.zzb + 1;
        Object[] objArr = this.zza;
        int length = objArr.length;
        int i2 = i + i;
        if (i2 > length) {
            this.zza = Arrays.copyOf(objArr, zzal.zza(length, i2));
        }
        zzah.zza(obj, obj2);
        Object[] objArr2 = this.zza;
        int i3 = this.zzb;
        int i4 = i3 + i3;
        objArr2[i4] = obj;
        objArr2[i4 + 1] = obj2;
        this.zzb = i3 + 1;
        return this;
    }

    public final zzat zzb() {
        zzar zzarVar = this.zzc;
        if (zzarVar == null) {
            zzbb zzg = zzbb.zzg(this.zzb, this.zza, this);
            zzar zzarVar2 = this.zzc;
            if (zzarVar2 == null) {
                return zzg;
            }
            throw zzarVar2.zza();
        }
        throw zzarVar.zza();
    }
}
