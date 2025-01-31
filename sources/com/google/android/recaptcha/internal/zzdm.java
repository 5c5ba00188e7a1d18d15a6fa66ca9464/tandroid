package com.google.android.recaptcha.internal;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import kotlin.collections.ArraysKt___ArraysKt;

/* loaded from: classes.dex */
public final class zzdm implements zzdd {
    public static final zzdm zza = new zzdm();

    private zzdm() {
    }

    @Override // com.google.android.recaptcha.internal.zzdd
    public final void zza(int i, zzcj zzcjVar, zzpq... zzpqVarArr) {
        List list;
        int length = zzpqVarArr.length;
        if (length == 0) {
            throw new zzae(4, 3, null);
        }
        Object zza2 = zzcjVar.zzc().zza(zzpqVarArr[0]);
        if (true != (zza2 instanceof Method)) {
            zza2 = null;
        }
        Method method = (Method) zza2;
        if (method == null) {
            throw new zzae(4, 5, null);
        }
        zzck zzc = zzcjVar.zzc();
        list = ArraysKt___ArraysKt.toList(zzpqVarArr);
        Object[] zzh = zzc.zzh(list.subList(1, length));
        try {
            zzcjVar.zzc().zzf(i, method.invoke(null, Arrays.copyOf(zzh, zzh.length)));
        } catch (Exception e) {
            throw new zzae(6, 15, e);
        }
    }
}
