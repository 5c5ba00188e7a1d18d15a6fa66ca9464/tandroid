package com.google.android.recaptcha.internal;

import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.ThreadPoolDispatcherKt;

/* loaded from: classes.dex */
public final class zzt {
    public static final zzr zza = new zzr(null);
    private final CoroutineScope zzb = CoroutineScopeKt.MainScope();
    private final CoroutineScope zzc;
    private final CoroutineScope zzd;

    public zzt() {
        CoroutineScope CoroutineScope = CoroutineScopeKt.CoroutineScope(ThreadPoolDispatcherKt.newSingleThreadContext("reCaptcha"));
        BuildersKt__Builders_commonKt.launch$default(CoroutineScope, null, null, new zzs(null), 3, null);
        this.zzc = CoroutineScope;
        this.zzd = CoroutineScopeKt.CoroutineScope(Dispatchers.getIO());
    }

    public final CoroutineScope zza() {
        return this.zzd;
    }

    public final CoroutineScope zzb() {
        return this.zzb;
    }

    public final CoroutineScope zzc() {
        return this.zzc;
    }
}
