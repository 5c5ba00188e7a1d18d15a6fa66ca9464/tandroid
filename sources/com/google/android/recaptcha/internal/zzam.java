package com.google.android.recaptcha.internal;

import android.app.Application;
import android.webkit.WebView;
import androidx.core.content.ContextCompat;
import com.google.android.gms.tasks.Task;
import com.google.android.recaptcha.RecaptchaErrorCode;
import com.google.android.recaptcha.RecaptchaException;
import java.util.UUID;
import kotlin.ResultKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.Deferred;
import kotlinx.coroutines.TimeoutKt;
import kotlinx.coroutines.sync.Mutex;
import kotlinx.coroutines.sync.MutexKt;

/* loaded from: classes.dex */
public final class zzam {
    private static zzaw zzb;
    public static final zzam zza = new zzam();
    private static final String zzc = UUID.randomUUID().toString();
    private static final Mutex zzd = MutexKt.Mutex$default(false, 1, null);
    private static final zzt zze = new zzt();
    private static zzg zzf = new zzg(null, 1, 0 == true ? 1 : 0);

    private zzam() {
    }

    public static final Object zzc(Application application, String str, long j, zzbq zzbqVar, Continuation continuation) {
        return BuildersKt.withContext(zze.zzb().getCoroutineContext(), new zzah(application, str, j, null, null), continuation);
    }

    public static final Task zzd(Application application, String str, long j) {
        Deferred async$default;
        async$default = BuildersKt__Builders_commonKt.async$default(zze.zzb(), null, null, new zzak(application, str, j, null), 3, null);
        return zzj.zza(async$default);
    }

    public static final zzg zze() {
        return zzf;
    }

    public static final void zzf(zzg zzgVar) {
        zzf = zzgVar;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:32:0x00d5 A[Catch: all -> 0x00fe, Exception -> 0x0103, RecaptchaException -> 0x0107, TryCatch #5 {RecaptchaException -> 0x0107, Exception -> 0x0103, all -> 0x00fe, blocks: (B:29:0x0090, B:32:0x00d5, B:34:0x00dd, B:36:0x00eb, B:38:0x00f5, B:39:0x010c, B:40:0x0130, B:41:0x0131), top: B:28:0x0090 }] */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0191 A[Catch: all -> 0x0171, RecaptchaException -> 0x0173, Exception -> 0x01b0, TryCatch #7 {RecaptchaException -> 0x0173, Exception -> 0x01b0, all -> 0x0171, blocks: (B:43:0x014f, B:48:0x0178, B:49:0x0190, B:50:0x0191, B:51:0x01ac), top: B:30:0x00d3 }] */
    /* JADX WARN: Removed duplicated region for block: B:66:0x0067  */
    /* JADX WARN: Removed duplicated region for block: B:9:0x002b  */
    /* JADX WARN: Type inference failed for: r0v4, types: [kotlinx.coroutines.sync.Mutex] */
    /* JADX WARN: Type inference failed for: r1v2, types: [com.google.android.recaptcha.internal.zzai, kotlin.coroutines.Continuation] */
    /* JADX WARN: Type inference failed for: r1v25 */
    /* JADX WARN: Type inference failed for: r1v26 */
    /* JADX WARN: Type inference failed for: r1v3, types: [kotlinx.coroutines.sync.Mutex] */
    /* JADX WARN: Type inference failed for: r1v4 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object zza(Application application, String str, long j, zzab zzabVar, WebView webView, zzbq zzbqVar, zzt zztVar, Continuation continuation) {
        ?? r1;
        Object coroutine_suspended;
        int i;
        Mutex mutex;
        zzt zztVar2;
        zzab zzabVar2;
        Application application2;
        long j2;
        String str2;
        Mutex mutex2;
        zzaw zzawVar;
        zzbd zzbdVar;
        zzbg zzbgVar;
        Mutex mutex3;
        try {
            try {
                try {
                    if (continuation instanceof zzai) {
                        zzai zzaiVar = (zzai) continuation;
                        int i2 = zzaiVar.zzg;
                        if ((i2 & Integer.MIN_VALUE) != 0) {
                            zzaiVar.zzg = i2 - Integer.MIN_VALUE;
                            r1 = zzaiVar;
                            Object obj = r1.zze;
                            coroutine_suspended = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
                            i = r1.zzg;
                            if (i != 0) {
                                ResultKt.throwOnFailure(obj);
                                ?? r0 = zzd;
                                r1.zza = application;
                                r1.zzb = str;
                                r1.zzc = zzabVar;
                                r1.zzi = zztVar;
                                r1.zzh = r0;
                                r1.zzd = j;
                                r1.zzg = 1;
                                if (r0.lock(null, r1) != coroutine_suspended) {
                                    mutex = r0;
                                    zztVar2 = zztVar;
                                    zzabVar2 = zzabVar;
                                    application2 = application;
                                    j2 = j;
                                    str2 = str;
                                }
                                return coroutine_suspended;
                            }
                            if (i != 1) {
                                if (i != 2) {
                                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                                }
                                zzbgVar = (zzbg) r1.zzc;
                                zzbdVar = (zzbd) r1.zzb;
                                mutex3 = (Mutex) r1.zza;
                                try {
                                    ResultKt.throwOnFailure(obj);
                                    zzawVar = (zzaw) obj;
                                    zzb = zzawVar;
                                    zzbgVar.zza(zzbdVar.zza(zzne.zzg));
                                    mutex = mutex3;
                                    mutex.unlock(null);
                                    return zzawVar;
                                } catch (RecaptchaException e) {
                                    throw e;
                                } catch (Exception unused) {
                                    throw new RecaptchaException(RecaptchaErrorCode.INTERNAL_ERROR, null, 2, null);
                                }
                            }
                            j2 = r1.zzd;
                            Mutex mutex4 = r1.zzh;
                            zzt zztVar3 = r1.zzi;
                            zzabVar2 = (zzab) r1.zzc;
                            str2 = (String) r1.zzb;
                            Application application3 = (Application) r1.zza;
                            ResultKt.throwOnFailure(obj);
                            mutex = mutex4;
                            zztVar2 = zztVar3;
                            application2 = application3;
                            String uuid = UUID.randomUUID().toString();
                            zzbd zzbdVar2 = new zzbd(zzc, uuid, null);
                            zzbdVar2.zzc(uuid);
                            zzbg zzbgVar2 = new zzbg(str2, application2, zzabVar2, zztVar2, new zzbm(application2, new zzbo(zzabVar2.zzc()), zztVar2.zza()));
                            zzne zzneVar = zzne.zzg;
                            zzbgVar2.zze.put(r8, new zzbf(zzbdVar2.zza(zzneVar), zzbgVar2.zza, new zzac()));
                            if (j2 >= 5000) {
                                zzbgVar2.zzb(zzbdVar2.zza(zzneVar), new zzp(zzn.zzm, zzl.zzT, null), null);
                                throw new RecaptchaException(RecaptchaErrorCode.INVALID_TIMEOUT, null, 2, null);
                            }
                            if (ContextCompat.checkSelfPermission(application2, "android.permission.INTERNET") != 0) {
                                zzbgVar2.zzb(zzbdVar2.zza(zzneVar), new zzp(zzn.zze, zzl.zzv, null), null);
                                throw new RecaptchaException(RecaptchaErrorCode.NETWORK_ERROR, null, 2, null);
                            }
                            zzbq zzbqVar2 = new zzbq(new zzy(application2), zzbgVar2);
                            zzawVar = zzb;
                            if (zzawVar != null) {
                                if (Intrinsics.areEqual(zzawVar.zzg(), str2)) {
                                    zzbgVar2.zza(zzbdVar2.zza(zzneVar));
                                    mutex.unlock(null);
                                    return zzawVar;
                                }
                                throw new RecaptchaException(RecaptchaErrorCode.INVALID_SITEKEY, "Only one site key can be used per runtime. The site key you provided " + str2 + " is different than " + zzawVar.zzg());
                            }
                            r1.zza = mutex;
                            r1.zzb = zzbdVar2;
                            r1.zzc = zzbgVar2;
                            r1.zzi = null;
                            r1.zzh = null;
                            r1.zzg = 2;
                            Mutex mutex5 = mutex;
                            Object withTimeout = TimeoutKt.withTimeout(j2, new zzaj(application2, zzabVar2, str2, zzbqVar2, zzbdVar2, zztVar2, null, zzbgVar2, j2, null), r1);
                            if (withTimeout != coroutine_suspended) {
                                zzbdVar = zzbdVar2;
                                zzbgVar = zzbgVar2;
                                obj = withTimeout;
                                mutex3 = mutex5;
                                zzawVar = (zzaw) obj;
                                zzb = zzawVar;
                                zzbgVar.zza(zzbdVar.zza(zzne.zzg));
                                mutex = mutex3;
                                mutex.unlock(null);
                                return zzawVar;
                            }
                            return coroutine_suspended;
                        }
                    }
                    if (j2 >= 5000) {
                    }
                } catch (RecaptchaException e2) {
                    e = e2;
                    throw e;
                } catch (Exception unused2) {
                    throw new RecaptchaException(RecaptchaErrorCode.INTERNAL_ERROR, null, 2, null);
                } catch (Throwable th) {
                    th = th;
                    r1 = mutex2;
                    r1.unlock(null);
                    throw th;
                }
                String uuid2 = UUID.randomUUID().toString();
                zzbd zzbdVar22 = new zzbd(zzc, uuid2, null);
                zzbdVar22.zzc(uuid2);
                zzbg zzbgVar22 = new zzbg(str2, application2, zzabVar2, zztVar2, new zzbm(application2, new zzbo(zzabVar2.zzc()), zztVar2.zza()));
                zzne zzneVar2 = zzne.zzg;
                zzbgVar22.zze.put(r8, new zzbf(zzbdVar22.zza(zzneVar2), zzbgVar22.zza, new zzac()));
            } catch (RecaptchaException e3) {
                e = e3;
            } catch (Exception unused3) {
            } catch (Throwable th2) {
                th = th2;
                mutex2 = mutex;
            }
            if (i != 0) {
            }
        } catch (Throwable th3) {
            th = th3;
        }
        r1 = new zzai(this, continuation);
        Object obj2 = r1.zze;
        coroutine_suspended = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = r1.zzg;
    }
}
