package com.google.android.recaptcha.internal;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Lambda;

/* loaded from: classes.dex */
final class zzcx extends Lambda implements Function2 {
    final /* synthetic */ zzcj zza;
    final /* synthetic */ String zzb;
    final /* synthetic */ int zzc;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    zzcx(zzcj zzcjVar, String str, int i) {
        super(2);
        this.zza = zzcjVar;
        this.zzb = str;
        this.zzc = i;
    }

    @Override // kotlin.jvm.functions.Function2
    public final /* bridge */ /* synthetic */ Object invoke(Object obj, Object obj2) {
        Object[] objArr = (Object[]) obj;
        this.zza.zzi().zzb(this.zzb, (String) obj2);
        int i = this.zzc;
        if (i != -1) {
            this.zza.zzc().zzf(i, objArr);
        }
        return Unit.INSTANCE;
    }
}
