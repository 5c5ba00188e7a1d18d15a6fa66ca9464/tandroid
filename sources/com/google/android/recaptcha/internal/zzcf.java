package com.google.android.recaptcha.internal;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;

/* loaded from: classes.dex */
public final class zzcf extends zzce {
    private final Function2 zza;
    private final String zzb;

    public zzcf(Function2 function2, String str, Object obj) {
        super(obj);
        this.zza = function2;
        this.zzb = str;
    }

    @Override // com.google.android.recaptcha.internal.zzce
    public final boolean zza(Object obj, Method method, Object[] objArr) {
        Collection emptyList;
        if (!Intrinsics.areEqual(method.getName(), this.zzb)) {
            return false;
        }
        zzpi zzf = zzpl.zzf();
        if (objArr != null) {
            emptyList = new ArrayList(objArr.length);
            for (Object obj2 : objArr) {
                zzpj zzf2 = zzpk.zzf();
                zzf2.zzv(obj2.toString());
                emptyList.add((zzpk) zzf2.zzj());
            }
        } else {
            emptyList = CollectionsKt__CollectionsKt.emptyList();
        }
        zzf.zzd(emptyList);
        zzpl zzplVar = (zzpl) zzf.zzj();
        Function2 function2 = this.zza;
        byte[] zzd = zzplVar.zzd();
        function2.invoke(objArr, zzfy.zzh().zzi(zzd, 0, zzd.length));
        return true;
    }
}
