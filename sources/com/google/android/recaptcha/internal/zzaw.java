package com.google.android.recaptcha.internal;

import android.app.Application;
import com.google.android.gms.tasks.Task;
import com.google.android.recaptcha.RecaptchaAction;
import com.google.android.recaptcha.RecaptchaClient;
import com.google.android.recaptcha.RecaptchaException;
import com.google.android.recaptcha.RecaptchaTasksClient;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
import kotlin.Pair;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.TuplesKt;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.collections.MapsKt__MapsJVMKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.ranges.RangesKt___RangesKt;
import kotlin.text.Regex;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.Deferred;
import kotlinx.coroutines.TimeoutKt;

/* loaded from: classes.dex */
public final class zzaw implements RecaptchaClient, RecaptchaTasksClient {
    public static final zzan zza = new zzan(null);
    private static final Regex zzb = new Regex("^[a-zA-Z0-9/_]{0,100}$");
    private final Application zzc;
    private final zzg zzd;
    private final String zze;
    private final zzab zzf;
    private final zzoe zzg;
    private final zzbd zzh;
    private final zzbg zzi;
    private final zzq zzj;
    private final zzbs zzk;
    private final zzt zzl;

    public zzaw(Application application, zzg zzgVar, String str, zzt zztVar, zzab zzabVar, zzoe zzoeVar, zzbd zzbdVar, zzbg zzbgVar, zzq zzqVar, zzbs zzbsVar) {
        this.zzc = application;
        this.zzd = zzgVar;
        this.zze = str;
        this.zzl = zztVar;
        this.zzf = zzabVar;
        this.zzg = zzoeVar;
        this.zzh = zzbdVar;
        this.zzi = zzbgVar;
        this.zzj = zzqVar;
        this.zzk = zzbsVar;
    }

    public static final /* synthetic */ void zzi(zzaw zzawVar, long j, RecaptchaAction recaptchaAction, zzbd zzbdVar) {
        zzbb zza2 = zzbdVar.zza(zzne.zzm);
        r0.zze.put(zza2, new zzbf(zza2, zzawVar.zzi.zza, new zzac()));
        zzp zzpVar = !zzb.matches(recaptchaAction.getAction()) ? new zzp(zzn.zzi, zzl.zzq, null) : null;
        if (j < 5000) {
            zzpVar = new zzp(zzn.zzc, zzl.zzT, null);
        }
        zzbg zzbgVar = zzawVar.zzi;
        if (zzpVar == null) {
            zzbgVar.zza(zza2);
        } else {
            zzbgVar.zzb(zza2, zzpVar, null);
            throw zzpVar;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0068  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x006b  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0038  */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0024  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object zzj(long j, String str, zzbd zzbdVar, Continuation continuation) {
        zzao zzaoVar;
        Object coroutine_suspended;
        int i;
        Exception e;
        zzaw zzawVar;
        zzbb zzbbVar;
        if (continuation instanceof zzao) {
            zzaoVar = (zzao) continuation;
            int i2 = zzaoVar.zzc;
            if ((i2 & Integer.MIN_VALUE) != 0) {
                zzaoVar.zzc = i2 - Integer.MIN_VALUE;
                Object obj = zzaoVar.zza;
                coroutine_suspended = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = zzaoVar.zzc;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj);
                    zzbb zza2 = zzbdVar.zza(zzne.zzp);
                    r10.zze.put(zza2, new zzbf(zza2, this.zzi.zza, new zzac()));
                    try {
                        zzg zzgVar = this.zzd;
                        zzaoVar.zzd = this;
                        zzaoVar.zze = zza2;
                        zzaoVar.zzc = 1;
                        obj = zzgVar.zza(str, j, zzaoVar);
                        if (obj == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        zzawVar = this;
                        zzbbVar = zza2;
                    } catch (Exception e2) {
                        e = e2;
                        zzawVar = this;
                        zzbbVar = zza2;
                        zzp zzpVar = !(e instanceof zzp) ? (zzp) e : new zzp(zzn.zzc, zzl.zzan, null);
                        zzawVar.zzi.zzb(zzbbVar, zzpVar, null);
                        throw zzpVar;
                    }
                } else {
                    if (i != 1) {
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                    }
                    zzbbVar = zzaoVar.zze;
                    zzawVar = zzaoVar.zzd;
                    try {
                        ResultKt.throwOnFailure(obj);
                    } catch (Exception e3) {
                        e = e3;
                        if (!(e instanceof zzp)) {
                        }
                        zzawVar.zzi.zzb(zzbbVar, zzpVar, null);
                        throw zzpVar;
                    }
                }
                zzog zzogVar = (zzog) obj;
                zzawVar.zzi.zza(zzbbVar);
                return zzogVar;
            }
        }
        zzaoVar = new zzao(this, continuation);
        Object obj2 = zzaoVar.zza;
        coroutine_suspended = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = zzaoVar.zzc;
        if (i != 0) {
        }
        zzog zzogVar2 = (zzog) obj2;
        zzawVar.zzi.zza(zzbbVar);
        return zzogVar2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0089  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x008c  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x003d  */
    /* JADX WARN: Removed duplicated region for block: B:9:0x0029  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object zzk(RecaptchaAction recaptchaAction, long j, Continuation continuation) {
        zzas zzasVar;
        Object coroutine_suspended;
        int i;
        zzaw zzawVar;
        zzbd zzbdVar;
        zzp zzpVar;
        if (continuation instanceof zzas) {
            zzasVar = (zzas) continuation;
            int i2 = zzasVar.zzc;
            if ((i2 & Integer.MIN_VALUE) != 0) {
                zzasVar.zzc = i2 - Integer.MIN_VALUE;
                zzas zzasVar2 = zzasVar;
                Object obj = zzasVar2.zza;
                coroutine_suspended = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = zzasVar2.zzc;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj);
                    String uuid = UUID.randomUUID().toString();
                    zzbd zzb2 = this.zzh.zzb();
                    zzb2.zzc(uuid);
                    r1.zze.put(r2, new zzbf(zzb2.zza(zzne.zzo), this.zzi.zza, new zzac()));
                    try {
                        zzat zzatVar = new zzat(this, j, recaptchaAction, zzb2, uuid, null);
                        zzasVar2.zzd = this;
                        zzasVar2.zze = zzb2;
                        zzasVar2.zzc = 1;
                        obj = TimeoutKt.withTimeout(j, zzatVar, zzasVar2);
                        if (obj == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        zzawVar = this;
                        zzbdVar = zzb2;
                    } catch (Exception e) {
                        e = e;
                        zzawVar = this;
                        zzbdVar = zzb2;
                        if (e instanceof zzp) {
                        }
                        zzawVar.zzi.zzb(zzbdVar.zza(zzne.zzo), zzpVar, null);
                        RecaptchaException zzc = zzpVar.zzc();
                        Result.Companion companion = Result.Companion;
                        return Result.constructor-impl(ResultKt.createFailure(zzc));
                    }
                } else {
                    if (i != 1) {
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                    }
                    zzbdVar = zzasVar2.zze;
                    zzawVar = zzasVar2.zzd;
                    try {
                        ResultKt.throwOnFailure(obj);
                    } catch (Exception e2) {
                        e = e2;
                        if (e instanceof zzp) {
                            zzpVar = new zzp(zzn.zzc, zzl.zzaj, e.getClass().getSimpleName());
                        } else {
                            zzpVar = (zzp) e;
                        }
                        zzawVar.zzi.zzb(zzbdVar.zza(zzne.zzo), zzpVar, null);
                        RecaptchaException zzc2 = zzpVar.zzc();
                        Result.Companion companion2 = Result.Companion;
                        return Result.constructor-impl(ResultKt.createFailure(zzc2));
                    }
                }
                return ((Result) obj).unbox-impl();
            }
        }
        zzasVar = new zzas(this, continuation);
        zzas zzasVar22 = zzasVar;
        Object obj2 = zzasVar22.zza;
        coroutine_suspended = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = zzasVar22.zzc;
        if (i != 0) {
        }
        return ((Result) obj2).unbox-impl();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void zzl(zzol zzolVar, zzbd zzbdVar) {
        int mapCapacity;
        int coerceAtLeast;
        zzbb zza2 = zzbdVar.zza(zzne.zzr);
        r0.zze.put(zza2, new zzbf(zza2, this.zzi.zza, new zzac()));
        try {
            List<zzon> zzj = zzolVar.zzj();
            mapCapacity = MapsKt__MapsJVMKt.mapCapacity(CollectionsKt__IterablesKt.collectionSizeOrDefault(zzj, 10));
            coerceAtLeast = RangesKt___RangesKt.coerceAtLeast(mapCapacity, 16);
            LinkedHashMap linkedHashMap = new LinkedHashMap(coerceAtLeast);
            for (zzon zzonVar : zzj) {
                Pair pair = TuplesKt.to(zzonVar.zzg(), zzonVar.zzi());
                linkedHashMap.put(pair.getFirst(), pair.getSecond());
            }
            this.zzj.zzb(linkedHashMap);
            this.zzi.zza(zza2);
        } catch (Exception e) {
            zzp zzpVar = e instanceof zzp ? (zzp) e : new zzp(zzn.zzc, zzl.zzan, null);
            this.zzi.zzb(zza2, zzpVar, null);
            throw zzpVar;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0031  */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0023  */
    @Override // com.google.android.recaptcha.RecaptchaClient
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object execute-0E7RQCE(RecaptchaAction recaptchaAction, long j, Continuation continuation) {
        zzap zzapVar;
        Object coroutine_suspended;
        int i;
        if (continuation instanceof zzap) {
            zzapVar = (zzap) continuation;
            int i2 = zzapVar.zzc;
            if ((i2 & Integer.MIN_VALUE) != 0) {
                zzapVar.zzc = i2 - Integer.MIN_VALUE;
                Object obj = zzapVar.zza;
                coroutine_suspended = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = zzapVar.zzc;
                if (i != 0) {
                    ResultKt.throwOnFailure(obj);
                    CoroutineContext coroutineContext = this.zzl.zzb().getCoroutineContext();
                    zzaq zzaqVar = new zzaq(this, recaptchaAction, j, null);
                    zzapVar.zzc = 1;
                    obj = BuildersKt.withContext(coroutineContext, zzaqVar, zzapVar);
                    if (obj == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                } else {
                    if (i != 1) {
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                    }
                    ResultKt.throwOnFailure(obj);
                }
                return ((Result) obj).unbox-impl();
            }
        }
        zzapVar = new zzap(this, continuation);
        Object obj2 = zzapVar.zza;
        coroutine_suspended = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = zzapVar.zzc;
        if (i != 0) {
        }
        return ((Result) obj2).unbox-impl();
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x0037  */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0023  */
    @Override // com.google.android.recaptcha.RecaptchaClient
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object execute-gIAlu-s(RecaptchaAction recaptchaAction, Continuation continuation) {
        zzar zzarVar;
        Object coroutine_suspended;
        int i;
        if (continuation instanceof zzar) {
            zzarVar = (zzar) continuation;
            int i2 = zzarVar.zzc;
            if ((i2 & Integer.MIN_VALUE) != 0) {
                zzarVar.zzc = i2 - Integer.MIN_VALUE;
                Object obj = zzarVar.zza;
                coroutine_suspended = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = zzarVar.zzc;
                if (i == 0) {
                    if (i != 1) {
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                    }
                    ResultKt.throwOnFailure(obj);
                    return ((Result) obj).unbox-impl();
                }
                ResultKt.throwOnFailure(obj);
                zzarVar.zzc = 1;
                Object obj2 = execute-0E7RQCE(recaptchaAction, 10000L, zzarVar);
                return obj2 == coroutine_suspended ? coroutine_suspended : obj2;
            }
        }
        zzarVar = new zzar(this, continuation);
        Object obj3 = zzarVar.zza;
        coroutine_suspended = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = zzarVar.zzc;
        if (i == 0) {
        }
    }

    @Override // com.google.android.recaptcha.RecaptchaTasksClient
    public final Task<String> executeTask(RecaptchaAction recaptchaAction) {
        Deferred async$default;
        async$default = BuildersKt__Builders_commonKt.async$default(this.zzl.zzb(), null, null, new zzau(this, recaptchaAction, 10000L, null), 3, null);
        return zzj.zza(async$default);
    }

    @Override // com.google.android.recaptcha.RecaptchaTasksClient
    public final Task<String> executeTask(RecaptchaAction recaptchaAction, long j) {
        Deferred async$default;
        async$default = BuildersKt__Builders_commonKt.async$default(this.zzl.zzb(), null, null, new zzau(this, recaptchaAction, j, null), 3, null);
        return zzj.zza(async$default);
    }

    public final String zzg() {
        return this.zze;
    }
}
