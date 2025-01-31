package com.google.android.recaptcha.internal;

import com.google.android.recaptcha.RecaptchaAction;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;

/* loaded from: classes.dex */
final class zzat extends SuspendLambda implements Function2 {
    int zza;
    final /* synthetic */ zzaw zzb;
    final /* synthetic */ long zzc;
    final /* synthetic */ RecaptchaAction zzd;
    final /* synthetic */ zzbd zze;
    final /* synthetic */ String zzf;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    zzat(zzaw zzawVar, long j, RecaptchaAction recaptchaAction, zzbd zzbdVar, String str, Continuation continuation) {
        super(2, continuation);
        this.zzb = zzawVar;
        this.zzc = j;
        this.zzd = recaptchaAction;
        this.zze = zzbdVar;
        this.zzf = str;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation create(Object obj, Continuation continuation) {
        return new zzat(this.zzb, this.zzc, this.zzd, this.zze, this.zzf, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final /* bridge */ /* synthetic */ Object invoke(Object obj, Object obj2) {
        return ((zzat) create((CoroutineScope) obj, (Continuation) obj2)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0029, code lost:
    
        if (r13 != r0) goto L8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:3:0x000c, code lost:
    
        if (r1 != 1) goto L11;
     */
    /* JADX WARN: Code restructure failed: missing block: B:5:0x003a, code lost:
    
        if (r13 == r0) goto L13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x0064, code lost:
    
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x003d, code lost:
    
        r13 = (com.google.android.recaptcha.internal.zzol) r13;
        r12.zzb.zzl(r13, r12.zze);
        r0 = r12.zzb;
        r1 = r12.zze;
        r0 = r0.zzi;
        r0.zza(r1.zza(com.google.android.recaptcha.internal.zzne.zzo));
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x0063, code lost:
    
        return kotlin.Result.box-impl(kotlin.Result.constructor-impl(r13.zzi()));
     */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object invokeSuspend(Object obj) {
        Object coroutine_suspended;
        coroutine_suspended = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.zza;
        ResultKt.throwOnFailure(obj);
        if (i == 0) {
            zzaw.zzi(this.zzb, this.zzc, this.zzd, this.zze);
            zzaw zzawVar = this.zzb;
            long j = this.zzc;
            String str = this.zzf;
            zzbd zzbdVar = this.zze;
            this.zza = 1;
            obj = zzawVar.zzj(j, str, zzbdVar, this);
        }
        zzaw zzawVar2 = this.zzb;
        RecaptchaAction recaptchaAction = this.zzd;
        this.zza = 2;
        obj = BuildersKt.withContext(zzawVar2.zzl.zza().getCoroutineContext(), new zzav(this.zze, zzawVar2, recaptchaAction, (zzog) obj, null), this);
    }
}
