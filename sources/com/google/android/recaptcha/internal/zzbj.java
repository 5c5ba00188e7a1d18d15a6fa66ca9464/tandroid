package com.google.android.recaptcha.internal;

import java.util.TimerTask;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.CoroutineScope;

/* loaded from: classes.dex */
public final class zzbj extends TimerTask {
    final /* synthetic */ zzbm zza;

    public zzbj(zzbm zzbmVar) {
        this.zza = zzbmVar;
    }

    @Override // java.util.TimerTask, java.lang.Runnable
    public final void run() {
        CoroutineScope coroutineScope;
        zzbm zzbmVar = this.zza;
        coroutineScope = zzbmVar.zzd;
        BuildersKt__Builders_commonKt.launch$default(coroutineScope, null, null, new zzbk(zzbmVar, null), 3, null);
    }
}
