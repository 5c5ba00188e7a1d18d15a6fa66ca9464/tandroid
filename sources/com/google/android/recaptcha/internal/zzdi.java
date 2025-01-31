package com.google.android.recaptcha.internal;

import java.util.Arrays;
import java.util.List;
import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.jvm.internal.Intrinsics;

/* loaded from: classes.dex */
public final class zzdi implements zzdd {
    public static final zzdi zza = new zzdi();

    private zzdi() {
    }

    @Override // com.google.android.recaptcha.internal.zzdd
    public final void zza(int i, zzcj zzcjVar, zzpq... zzpqVarArr) {
        List list;
        int length = zzpqVarArr.length;
        if (length < 2) {
            throw new zzae(4, 3, null);
        }
        Class<?> zza2 = zzcjVar.zzc().zza(zzpqVarArr[0]);
        if (true != (zza2 instanceof Object)) {
            zza2 = null;
        }
        if (zza2 == null) {
            throw new zzae(4, 5, null);
        }
        Class<?> cls = zza2 instanceof Class ? zza2 : zza2.getClass();
        Object zza3 = zzcjVar.zzc().zza(zzpqVarArr[1]);
        if (true != (zza3 instanceof String)) {
            zza3 = null;
        }
        String str = (String) zza3;
        if (str == null) {
            throw new zzae(4, 5, null);
        }
        String zza4 = zzcjVar.zzh().zza(str);
        if (Intrinsics.areEqual(zza4, "forName")) {
            throw new zzae(6, 48, null);
        }
        zzck zzc = zzcjVar.zzc();
        list = ArraysKt___ArraysKt.toList(zzpqVarArr);
        Class[] zzg = zzc.zzg(list.subList(2, length));
        try {
            zzcjVar.zzc().zzf(i, cls.getMethod(zza4, (Class[]) Arrays.copyOf(zzg, zzg.length)));
        } catch (Exception e) {
            throw new zzae(6, 13, e);
        }
    }
}
