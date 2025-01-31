package com.google.android.recaptcha.internal;

import kotlin.jvm.internal.Intrinsics;

/* loaded from: classes.dex */
public final class zzcp implements zzdd {
    public static final zzcp zza = new zzcp();

    private zzcp() {
    }

    @Override // com.google.android.recaptcha.internal.zzdd
    public final void zza(int i, zzcj zzcjVar, zzpq... zzpqVarArr) {
        if (zzpqVarArr.length != 3) {
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
        if (intValue == 0) {
            throw new zzae(4, 6, null);
        }
        Object zza3 = zzcjVar.zzc().zza(zzpqVarArr[1]);
        if (true != (zza3 instanceof Object)) {
            zza3 = null;
        }
        if (zza3 == null) {
            throw new zzae(4, 5, null);
        }
        Object zza4 = zzcjVar.zzc().zza(zzpqVarArr[2]);
        if (true != (zza4 instanceof Object)) {
            zza4 = null;
        }
        if (zza4 == null) {
            throw new zzae(4, 5, null);
        }
        if (Intrinsics.areEqual(zza3, zza4)) {
            zzcjVar.zzg(zzcjVar.zza() + intValue);
        }
    }
}
