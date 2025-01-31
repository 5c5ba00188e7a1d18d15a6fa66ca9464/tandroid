package com.google.android.recaptcha.internal;

import com.google.android.recaptcha.RecaptchaAction;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CoroutineScope;

/* loaded from: classes.dex */
final class zzav extends SuspendLambda implements Function2 {
    final /* synthetic */ zzbd zza;
    final /* synthetic */ zzaw zzb;
    final /* synthetic */ RecaptchaAction zzc;
    final /* synthetic */ zzog zzd;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    zzav(zzbd zzbdVar, zzaw zzawVar, RecaptchaAction recaptchaAction, zzog zzogVar, Continuation continuation) {
        super(2, continuation);
        this.zza = zzbdVar;
        this.zzb = zzawVar;
        this.zzc = recaptchaAction;
        this.zzd = zzogVar;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation create(Object obj, Continuation continuation) {
        return new zzav(this.zza, this.zzb, this.zzc, this.zzd, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final /* bridge */ /* synthetic */ Object invoke(Object obj, Object obj2) {
        return ((zzav) create((CoroutineScope) obj, (Continuation) obj2)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        zzbg zzbgVar;
        zzoe zzoeVar;
        zzoe zzoeVar2;
        zzbg zzbgVar2;
        zzab zzabVar;
        zzbg zzbgVar3;
        IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        ResultKt.throwOnFailure(obj);
        zzbb zza = this.zza.zza(zzne.zzq);
        zzbgVar = this.zzb.zzi;
        zzbgVar.zze.put(zza, new zzbf(zza, zzbgVar.zza, new zzac()));
        zzob zzf = zzoc.zzf();
        zzaw zzawVar = this.zzb;
        zzf.zzr(zzawVar.zzg());
        zzf.zzd(this.zzc.getAction());
        zzoeVar = zzawVar.zzg;
        zzf.zzq(zzoeVar.zzI());
        zzoeVar2 = zzawVar.zzg;
        zzf.zzp(zzoeVar2.zzH());
        zzog zzogVar = this.zzd;
        zzf.zzt(zzogVar.zzH());
        zzf.zze(zzogVar.zzj());
        zzf.zzs(zzogVar.zzI());
        zzoc zzocVar = (zzoc) zzf.zzj();
        try {
            zzabVar = this.zzb.zzf;
            URLConnection openConnection = new URL(zzabVar.zzd()).openConnection();
            Intrinsics.checkNotNull(openConnection, "null cannot be cast to non-null type java.net.HttpURLConnection");
            HttpURLConnection httpURLConnection = (HttpURLConnection) openConnection;
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestProperty("Content-Type", "application/x-protobuffer");
            try {
                httpURLConnection.connect();
                zzoi zzf2 = zzoj.zzf();
                zzf2.zzd(zzocVar.zzi());
                zzf2.zzq(zzocVar.zzk());
                zzf2.zzt(zzocVar.zzI());
                zzf2.zzp(zzocVar.zzH());
                zzf2.zzr(zzocVar.zzJ());
                zzf2.zzs(zzocVar.zzK());
                zzf2.zze(zzocVar.zzj());
                httpURLConnection.getOutputStream().write(((zzoj) zzf2.zzj()).zzd());
                if (httpURLConnection.getResponseCode() != 200) {
                    throw zzbr.zza(httpURLConnection.getResponseCode());
                }
                try {
                    zzol zzg = zzol.zzg(httpURLConnection.getInputStream());
                    zzbgVar3 = this.zzb.zzi;
                    zzbgVar3.zza(zza);
                    return zzg;
                } catch (Exception unused) {
                    throw new zzp(zzn.zzc, zzl.zzR, null);
                }
            } catch (Exception e) {
                if (e instanceof zzp) {
                    throw ((zzp) e);
                }
                throw new zzp(zzn.zze, zzl.zzQ, null);
            }
        } catch (Exception e2) {
            zzp zzpVar = e2 instanceof zzp ? (zzp) e2 : new zzp(zzn.zzc, zzl.zzam, null);
            zzbgVar2 = this.zzb.zzi;
            zzbgVar2.zzb(zza, zzpVar, null);
            throw zzpVar;
        }
    }
}
