package com.google.android.recaptcha.internal;

import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CompletableDeferred;
import kotlinx.coroutines.CoroutineScope;

/* loaded from: classes.dex */
final class zzew extends SuspendLambda implements Function2 {
    int zza;
    final /* synthetic */ zzez zzb;
    final /* synthetic */ zzoe zzc;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    zzew(zzez zzezVar, zzoe zzoeVar, Continuation continuation) {
        super(2, continuation);
        this.zzb = zzezVar;
        this.zzc = zzoeVar;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation create(Object obj, Continuation continuation) {
        return new zzew(this.zzb, this.zzc, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final /* bridge */ /* synthetic */ Object invoke(Object obj, Object obj2) {
        return ((zzew) create((CoroutineScope) obj, (Continuation) obj2)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        Object coroutine_suspended;
        zzbg zzbgVar;
        zzbd zzbdVar;
        zzfh zzfhVar;
        zzfh zzfhVar2;
        coroutine_suspended = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.zza;
        ResultKt.throwOnFailure(obj);
        if (i == 0) {
            zzez zzezVar = this.zzb;
            zzbgVar = zzezVar.zzi;
            zzbdVar = zzezVar.zzp;
            zzbgVar.zza(zzbdVar.zza(zzne.zzb));
            zzcb.zza(zznz.zzj(zzfy.zzh().zzj(this.zzc.zzJ())));
            zzfhVar = this.zzb.zzn;
            zzfhVar.zzd();
            zzfhVar2 = this.zzb.zzn;
            zzfhVar2.zze();
            zzez.zzl(this.zzb, this.zzc);
            Boxing.boxInt(this.zzb.zzk().hashCode());
            CompletableDeferred zzk = this.zzb.zzk();
            this.zza = 1;
            if (zzk.await(this) == coroutine_suspended) {
                return coroutine_suspended;
            }
        }
        return Result.box-impl(Result.constructor-impl(Unit.INSTANCE));
    }
}
