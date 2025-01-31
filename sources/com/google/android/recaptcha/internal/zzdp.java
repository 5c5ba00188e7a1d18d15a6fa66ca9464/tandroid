package com.google.android.recaptcha.internal;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import kotlin.collections.ArraysKt___ArraysKt;

/* loaded from: classes.dex */
public final class zzdp implements zzdd {
    public static final zzdp zza = new zzdp();

    private zzdp() {
    }

    @Override // com.google.android.recaptcha.internal.zzdd
    public final void zza(int i, zzcj zzcjVar, zzpq... zzpqVarArr) {
        List list;
        int length = zzpqVarArr.length;
        if (length == 0) {
            throw new zzae(4, 3, null);
        }
        Constructor<?> zza2 = zzcjVar.zzc().zza(zzpqVarArr[0]);
        if (true != (zza2 instanceof Object)) {
            zza2 = null;
        }
        if (zza2 == null) {
            throw new zzae(4, 5, null);
        }
        Constructor<?> constructor = zza2 instanceof Constructor ? zza2 : zza2.getClass().getConstructor(null);
        zzck zzc = zzcjVar.zzc();
        list = ArraysKt___ArraysKt.toList(zzpqVarArr);
        Object[] zzh = zzc.zzh(list.subList(1, length));
        try {
            zzcjVar.zzc().zzf(i, constructor.newInstance(Arrays.copyOf(zzh, zzh.length)));
        } catch (Exception e) {
            throw new zzae(6, 14, e);
        }
    }
}
