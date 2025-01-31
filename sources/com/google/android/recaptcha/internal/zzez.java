package com.google.android.recaptcha.internal;

import android.content.Context;
import android.webkit.WebView;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.collections.CollectionsKt__CollectionsJVMKt;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.CompletableDeferred;
import kotlinx.coroutines.CompletableDeferredKt;
import kotlinx.coroutines.TimeoutCancellationException;
import kotlinx.coroutines.TimeoutKt;

/* loaded from: classes.dex */
public final class zzez implements zza {
    public static final zzep zza = new zzep(null);
    public CompletableDeferred zzb;
    public zzbu zzc;
    private final WebView zzd;
    private final String zze;
    private final Context zzf;
    private final zzab zzg;
    private final zzbd zzh;
    private final zzbg zzi;
    private final zzbq zzj;
    private final Map zzk = zzfa.zza();
    private final Map zzl;
    private final Map zzm;
    private final zzfh zzn;
    private final zzeq zzo;
    private final zzbd zzp;
    private final zzt zzq;

    public zzez(WebView webView, String str, Context context, zzab zzabVar, zzbd zzbdVar, zzt zztVar, zzbg zzbgVar, zzbq zzbqVar) {
        this.zzd = webView;
        this.zze = str;
        this.zzf = context;
        this.zzg = zzabVar;
        this.zzh = zzbdVar;
        this.zzq = zztVar;
        this.zzi = zzbgVar;
        this.zzj = zzbqVar;
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        this.zzl = linkedHashMap;
        this.zzm = linkedHashMap;
        this.zzn = zzfh.zzc();
        zzeq zzeqVar = new zzeq(this);
        this.zzo = zzeqVar;
        zzbd zzb = zzbdVar.zzb();
        zzb.zzc(zzbdVar.zzd());
        this.zzp = zzb;
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(zzeqVar, "RN");
        webView.setWebViewClient(new zzeu(this));
    }

    public static final /* synthetic */ void zzl(zzez zzezVar, zzoe zzoeVar) {
        zzezVar.zzd.clearCache(true);
        zzbb zza2 = zzezVar.zzp.zza(zzne.zzc);
        r1.zze.put(zza2, new zzbf(zza2, zzezVar.zzi.zza, new zzac()));
        BuildersKt__Builders_commonKt.launch$default(zzezVar.zzq.zza(), null, null, new zzey(zzezVar, zzoeVar, zza2, null), 3, null);
    }

    public static final /* synthetic */ void zzm(zzez zzezVar, String str) {
        zzbb zza2 = zzezVar.zzp.zza(zzne.zzl);
        try {
            r2.zze.put(zza2, new zzbf(zza2, zzezVar.zzi.zza, new zzac()));
            zzezVar.zzd.loadDataWithBaseURL(zzezVar.zzg.zza(), str, "text/html", "utf-8", null);
        } catch (Exception unused) {
            zzp zzpVar = new zzp(zzn.zzc, zzl.zzag, null);
            zzezVar.zzi.zzb(zza2, zzpVar, null);
            zzezVar.zzk().completeExceptionally(zzpVar);
        }
    }

    private final zzp zzp(Exception exc, zzp zzpVar) {
        return exc instanceof TimeoutCancellationException ? new zzp(zzn.zzc, zzl.zzj, null) : exc instanceof zzp ? (zzp) exc : zzpVar;
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x0077  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0037  */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0023  */
    @Override // com.google.android.recaptcha.internal.zza
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object zza(String str, long j, Continuation continuation) {
        zzer zzerVar;
        Object coroutine_suspended;
        int i;
        Exception e;
        zzez zzezVar;
        CompletableDeferred completableDeferred;
        if (continuation instanceof zzer) {
            zzerVar = (zzer) continuation;
            int i2 = zzerVar.zzc;
            if ((i2 & Integer.MIN_VALUE) != 0) {
                zzerVar.zzc = i2 - Integer.MIN_VALUE;
                Object obj = zzerVar.zza;
                coroutine_suspended = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = zzerVar.zzc;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj);
                    try {
                        zzet zzetVar = new zzet(str, this, null);
                        zzerVar.zzd = this;
                        zzerVar.zze = str;
                        zzerVar.zzc = 1;
                        obj = TimeoutKt.withTimeout(j, zzetVar, zzerVar);
                        if (obj == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        zzezVar = this;
                    } catch (Exception e2) {
                        e = e2;
                        zzezVar = this;
                        zzp zzp = zzezVar.zzp(e, new zzp(zzn.zzc, zzl.zzai, e.getClass().getSimpleName()));
                        completableDeferred = (CompletableDeferred) zzezVar.zzl.remove(str);
                        if (completableDeferred != null) {
                        }
                        Result.Companion companion = Result.Companion;
                        return Result.constructor-impl(ResultKt.createFailure(zzp));
                    }
                } else {
                    if (i != 1) {
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                    }
                    str = zzerVar.zze;
                    zzezVar = zzerVar.zzd;
                    try {
                        ResultKt.throwOnFailure(obj);
                    } catch (Exception e3) {
                        e = e3;
                        zzp zzp2 = zzezVar.zzp(e, new zzp(zzn.zzc, zzl.zzai, e.getClass().getSimpleName()));
                        completableDeferred = (CompletableDeferred) zzezVar.zzl.remove(str);
                        if (completableDeferred != null) {
                            Boxing.boxBoolean(completableDeferred.completeExceptionally(zzp2));
                        }
                        Result.Companion companion2 = Result.Companion;
                        return Result.constructor-impl(ResultKt.createFailure(zzp2));
                    }
                }
                return Result.constructor-impl((zzog) obj);
            }
        }
        zzerVar = new zzer(this, continuation);
        Object obj2 = zzerVar.zza;
        coroutine_suspended = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = zzerVar.zzc;
        if (i != 0) {
        }
        return Result.constructor-impl((zzog) obj2);
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x008e  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x00e4 A[LOOP:0: B:27:0x00de->B:29:0x00e4, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:34:0x009e  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0039  */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0025  */
    @Override // com.google.android.recaptcha.internal.zza
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object zzb(long j, zzoe zzoeVar, Continuation continuation) {
        zzev zzevVar;
        Object coroutine_suspended;
        int i;
        Exception e;
        zzez zzezVar;
        Iterator it;
        if (continuation instanceof zzev) {
            zzevVar = (zzev) continuation;
            int i2 = zzevVar.zzd;
            if ((i2 & Integer.MIN_VALUE) != 0) {
                zzevVar.zzd = i2 - Integer.MIN_VALUE;
                Object obj = zzevVar.zzb;
                coroutine_suspended = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = zzevVar.zzd;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj);
                    try {
                        r11.zze.put(r4, new zzbf(this.zzp.zza(zzne.zzb), this.zzi.zza, new zzac()));
                        this.zzc = zzo(zzoeVar, new zzag(zzoeVar.zzf()));
                        this.zzb = CompletableDeferredKt.CompletableDeferred$default(null, 1, null);
                        Boxing.boxInt(zzk().hashCode());
                        zzew zzewVar = new zzew(this, zzoeVar, null);
                        zzevVar.zze = this;
                        zzevVar.zza = j;
                        zzevVar.zzd = 1;
                        obj = TimeoutKt.withTimeout(j, zzewVar, zzevVar);
                        if (obj == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        zzezVar = this;
                    } catch (Exception e2) {
                        e = e2;
                        zzezVar = this;
                        e.getMessage();
                        boolean z = e instanceof TimeoutCancellationException;
                        if (!z) {
                        }
                        Long zza2 = zzezVar.zzo.zza();
                        if (z) {
                            it = r0.iterator();
                            while (it.hasNext()) {
                            }
                            Result.Companion companion = Result.Companion;
                            return Result.constructor-impl(ResultKt.createFailure(r8.zzc()));
                        }
                        it = r0.iterator();
                        while (it.hasNext()) {
                        }
                        Result.Companion companion2 = Result.Companion;
                        return Result.constructor-impl(ResultKt.createFailure(r8.zzc()));
                    }
                } else {
                    if (i != 1) {
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                    }
                    j = zzevVar.zza;
                    zzezVar = zzevVar.zze;
                    try {
                        ResultKt.throwOnFailure(obj);
                    } catch (Exception e3) {
                        e = e3;
                        e.getMessage();
                        boolean z2 = e instanceof TimeoutCancellationException;
                        List listOf = !z2 ? CollectionsKt__CollectionsKt.listOf((Object[]) new zzne[]{zzne.zzg, zzne.zzl}) : CollectionsKt__CollectionsJVMKt.listOf(zzne.zzg);
                        Long zza22 = zzezVar.zzo.zza();
                        zzp zzpVar = (z2 && (zza22 == null || zza22.longValue() > j + (-2000))) ? new zzp(zzn.zze, zzl.zzS, null) : zzezVar.zzp(e, new zzp(zzn.zzc, zzl.zzah, e.getClass().getSimpleName()));
                        it = listOf.iterator();
                        while (it.hasNext()) {
                            zzezVar.zzi.zzb(zzezVar.zzp.zza((zzne) it.next()), zzpVar, null);
                        }
                        Result.Companion companion22 = Result.Companion;
                        return Result.constructor-impl(ResultKt.createFailure(zzpVar.zzc()));
                    }
                }
                return ((Result) obj).unbox-impl();
            }
        }
        zzevVar = new zzev(this, continuation);
        Object obj2 = zzevVar.zzb;
        coroutine_suspended = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = zzevVar.zzd;
        if (i != 0) {
        }
        return ((Result) obj2).unbox-impl();
    }

    public final WebView zzc() {
        return this.zzd;
    }

    public final zzbq zzf() {
        return this.zzj;
    }

    public final zzeq zzg() {
        return this.zzo;
    }

    public final CompletableDeferred zzk() {
        CompletableDeferred completableDeferred = this.zzb;
        if (completableDeferred != null) {
            return completableDeferred;
        }
        return null;
    }

    public final zzca zzo(zzoe zzoeVar, zzag zzagVar) {
        long[] longArray;
        zzcd zzcdVar = new zzcd(this.zzd, this.zzq.zzb());
        zzef zzefVar = new zzef();
        longArray = CollectionsKt___CollectionsKt.toLongArray(zzoeVar.zzK());
        zzefVar.zzb(longArray);
        zzcl zzclVar = new zzcl(zzcdVar, zzagVar, new zzaa());
        zzeg zzegVar = new zzeg(zzefVar, new zzed());
        zzclVar.zzf(3, this.zzf);
        zzclVar.zzf(5, zzen.class.getMethod("cs", new Object[0].getClass()));
        zzclVar.zzf(6, new zzeh(this.zzf));
        zzclVar.zzf(7, new zzej());
        zzclVar.zzf(8, new zzeo(this.zzf));
        zzclVar.zzf(9, new zzek(this.zzf));
        zzclVar.zzf(10, new zzei(this.zzf));
        return new zzca(this.zzq.zzc(), zzclVar, zzegVar, zzbt.zza());
    }
}
