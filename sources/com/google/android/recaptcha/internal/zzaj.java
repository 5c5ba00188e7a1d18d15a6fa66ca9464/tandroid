package com.google.android.recaptcha.internal;

import android.app.Application;
import android.webkit.WebView;
import java.util.List;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.sequences.SequencesKt___SequencesKt;
import kotlinx.coroutines.AwaitKt;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.JobKt;
import kotlinx.coroutines.JobKt__JobKt;

/* loaded from: classes.dex */
final class zzaj extends SuspendLambda implements Function2 {
    Object zza;
    int zzb;
    final /* synthetic */ Application zzc;
    final /* synthetic */ zzab zzd;
    final /* synthetic */ String zze;
    final /* synthetic */ zzbq zzf;
    final /* synthetic */ zzbd zzg;
    final /* synthetic */ zzbg zzh;
    final /* synthetic */ long zzi;
    final /* synthetic */ zzt zzj;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    zzaj(Application application, zzab zzabVar, String str, zzbq zzbqVar, zzbd zzbdVar, zzt zztVar, WebView webView, zzbg zzbgVar, long j, Continuation continuation) {
        super(2, continuation);
        this.zzc = application;
        this.zzd = zzabVar;
        this.zze = str;
        this.zzf = zzbqVar;
        this.zzg = zzbdVar;
        this.zzj = zztVar;
        this.zzh = zzbgVar;
        this.zzi = j;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation create(Object obj, Continuation continuation) {
        return new zzaj(this.zzc, this.zzd, this.zze, this.zzf, this.zzg, this.zzj, null, this.zzh, this.zzi, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final /* bridge */ /* synthetic */ Object invoke(Object obj, Object obj2) {
        return ((zzaj) create((CoroutineScope) obj, (Continuation) obj2)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x008e, code lost:
    
        if (r3 != r1) goto L7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0058, code lost:
    
        if (r2 != r1) goto L12;
     */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object invokeSuspend(Object obj) {
        Object coroutine_suspended;
        Object withContext;
        zzoe zzoeVar;
        Object zzb;
        Throwable th;
        List list;
        coroutine_suspended = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.zzb;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            zzam zzamVar = zzam.zza;
            Application application = this.zzc;
            zzab zzabVar = this.zzd;
            String str = this.zze;
            zzbq zzbqVar = this.zzf;
            zzbd zzbdVar = this.zzg;
            zzt zztVar = this.zzj;
            this.zzb = 1;
            withContext = BuildersKt.withContext(zztVar.zza().getCoroutineContext(), new zzal(application, str, zzbdVar, zzbqVar, zzabVar, null), this);
        } else {
            if (i != 1) {
                if (i != 2) {
                    th = (Throwable) this.zza;
                    ResultKt.throwOnFailure(obj);
                    zzam zzamVar2 = zzam.zza;
                    zzam.zzf(new zzg(null, 1, null));
                    throw th;
                }
                zzoeVar = (zzoe) this.zza;
                ResultKt.throwOnFailure(obj);
                zzb = ((Result) obj).unbox-impl();
                zzoe zzoeVar2 = zzoeVar;
                zzt zztVar2 = this.zzj;
                Throwable th2 = Result.exceptionOrNull-impl(zzb);
                if (th2 == null) {
                    Application application2 = this.zzc;
                    zzam zzamVar3 = zzam.zza;
                    return new zzaw(application2, zzam.zze(), this.zze, this.zzj, this.zzd, zzoeVar2, this.zzg, this.zzh, new zzq(application2), new zzbs());
                }
                JobKt__JobKt.cancelChildren$default(zztVar2.zzc().getCoroutineContext(), null, 1, null);
                list = SequencesKt___SequencesKt.toList(JobKt.getJob(zztVar2.zzc().getCoroutineContext()).getChildren());
                this.zza = th2;
                this.zzb = 3;
                if (AwaitKt.joinAll(list, this) != coroutine_suspended) {
                    th = th2;
                    zzam zzamVar22 = zzam.zza;
                    zzam.zzf(new zzg(null, 1, null));
                    throw th;
                }
                return coroutine_suspended;
            }
            ResultKt.throwOnFailure(obj);
            withContext = obj;
        }
        zzoeVar = (zzoe) withContext;
        zzam.zze().zzd(new zzez(new WebView(this.zzc), this.zze, this.zzc, this.zzd, this.zzg, this.zzj, this.zzh, this.zzf));
        long j = this.zzi;
        zzg zze = zzam.zze();
        this.zza = zzoeVar;
        this.zzb = 2;
        zzb = zze.zzb(j, zzoeVar, this);
    }
}
