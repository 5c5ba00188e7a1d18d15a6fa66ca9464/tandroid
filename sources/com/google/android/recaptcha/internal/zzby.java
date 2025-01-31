package com.google.android.recaptcha.internal;

import java.util.Arrays;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Reflection;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineScopeKt;

/* loaded from: classes.dex */
final class zzby extends SuspendLambda implements Function2 {
    final /* synthetic */ Exception zza;
    final /* synthetic */ zzcj zzb;
    final /* synthetic */ zzca zzc;
    private /* synthetic */ Object zzd;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    zzby(Exception exc, zzcj zzcjVar, zzca zzcaVar, Continuation continuation) {
        super(2, continuation);
        this.zza = exc;
        this.zzb = zzcjVar;
        this.zzc = zzcaVar;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation create(Object obj, Continuation continuation) {
        zzby zzbyVar = new zzby(this.zza, this.zzb, this.zzc, continuation);
        zzbyVar.zzd = obj;
        return zzbyVar;
    }

    @Override // kotlin.jvm.functions.Function2
    public final /* bridge */ /* synthetic */ Object invoke(Object obj, Object obj2) {
        return ((zzby) create((CoroutineScope) obj, (Continuation) obj2)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        zzpg zzpgVar;
        IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        ResultKt.throwOnFailure(obj);
        CoroutineScope coroutineScope = (CoroutineScope) this.zzd;
        Exception exc = this.zza;
        if (exc instanceof zzae) {
            zzpgVar = ((zzae) exc).zza();
            zzpgVar.zzd(this.zzb.zza());
        } else {
            zzcj zzcjVar = this.zzb;
            zzpg zzf = zzph.zzf();
            zzf.zzd(zzcjVar.zza());
            zzf.zzp(2);
            zzf.zze(2);
            zzpgVar = zzf;
        }
        zzph zzphVar = (zzph) zzpgVar.zzj();
        zzphVar.zzk();
        zzphVar.zzj();
        Reflection.getOrCreateKotlinClass(this.zza.getClass()).getSimpleName();
        this.zza.getMessage();
        zzcj zzcjVar2 = this.zzb;
        zzz zzb = zzcjVar2.zzb();
        zzz zzzVar = zzcjVar2.zza;
        if (zzzVar == null) {
            zzzVar = null;
        }
        zzno zza = zzbp.zza(zzb, zzzVar);
        String zzd = this.zzb.zzd();
        if (zzd.length() == 0) {
            zzd = "recaptcha.m.Main.rge";
        }
        if (CoroutineScopeKt.isActive(coroutineScope)) {
            zzca zzcaVar = this.zzc;
            zzfy zzh = zzfy.zzh();
            byte[] zzd2 = zzphVar.zzd();
            String zzi = zzh.zzi(zzd2, 0, zzd2.length);
            zzfy zzh2 = zzfy.zzh();
            byte[] zzd3 = zza.zzd();
            zzcaVar.zzc.zze().zzb(zzd, (String[]) Arrays.copyOf(new String[]{zzi, zzh2.zzi(zzd3, 0, zzd3.length)}, 2));
        }
        return Unit.INSTANCE;
    }
}
