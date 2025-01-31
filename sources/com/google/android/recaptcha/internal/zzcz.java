package com.google.android.recaptcha.internal;

import java.lang.reflect.Proxy;

/* loaded from: classes.dex */
public final class zzcz implements zzdd {
    public static final zzcz zza = new zzcz();

    private zzcz() {
    }

    @Override // com.google.android.recaptcha.internal.zzdd
    public final void zza(int i, zzcj zzcjVar, zzpq... zzpqVarArr) {
        int length = zzpqVarArr.length;
        if (length != 4 && length != 5) {
            throw new zzae(4, 3, null);
        }
        Object zza2 = zzcjVar.zzc().zza(zzpqVarArr[0]);
        if (true != (zza2 instanceof Integer)) {
            zza2 = null;
        }
        Integer num = (Integer) zza2;
        if (num == null) {
            throw new zzae(4, 5, null);
        }
        int intValue = num.intValue();
        Object zza3 = zzcjVar.zzc().zza(zzpqVarArr[1]);
        if (true != (zza3 instanceof Integer)) {
            zza3 = null;
        }
        Integer num2 = (Integer) zza3;
        if (num2 == null) {
            throw new zzae(4, 5, null);
        }
        int intValue2 = num2.intValue();
        Object zza4 = zzcjVar.zzc().zza(zzpqVarArr[2]);
        if (true != (zza4 instanceof String)) {
            zza4 = null;
        }
        String str = (String) zza4;
        if (str == null) {
            throw new zzae(4, 5, null);
        }
        String zza5 = zzcjVar.zzh().zza(str);
        Object zza6 = zzcjVar.zzc().zza(zzpqVarArr[3]);
        if (true != (zza6 instanceof String)) {
            zza6 = null;
        }
        String str2 = (String) zza6;
        if (str2 == null) {
            throw new zzae(4, 5, null);
        }
        String zza7 = zzcjVar.zzh().zza(str2);
        Object zza8 = length == 5 ? zzcjVar.zzc().zza(zzpqVarArr[4]) : null;
        zzcg zzcgVar = new zzcg(intValue2);
        try {
            Class zza9 = zzci.zza(zza5);
            zzcjVar.zzc().zzf(intValue, Proxy.newProxyInstance(zza9.getClassLoader(), new Class[]{zza9}, new zzch(zzcgVar, zza7, zza8)));
            zzcjVar.zzc().zzf(i, zzcgVar);
        } catch (Exception e) {
            throw new zzae(6, 20, e);
        }
    }
}
