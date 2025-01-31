package com.google.android.recaptcha;

import android.app.Application;
import com.google.android.gms.tasks.Task;
import com.google.android.recaptcha.internal.zzam;
import com.google.android.recaptcha.internal.zzaw;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;

/* loaded from: classes.dex */
public final class Recaptcha {
    public static final Recaptcha INSTANCE = new Recaptcha();

    private Recaptcha() {
    }

    public static /* synthetic */ Object getClient-BWLJW6A$default(Recaptcha recaptcha, Application application, String str, long j, Continuation continuation, int i, Object obj) {
        if ((i & 4) != 0) {
            j = 10000;
        }
        return recaptcha.getClient-BWLJW6A(application, str, j, continuation);
    }

    public static final Task<RecaptchaTasksClient> getTasksClient(Application application, String str) {
        return zzam.zzd(application, str, 10000L);
    }

    public static final Task<RecaptchaTasksClient> getTasksClient(Application application, String str, long j) {
        return zzam.zzd(application, str, j);
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0025  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0035  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object getClient-BWLJW6A(Application application, String str, long j, Continuation continuation) {
        Recaptcha$getClient$1 recaptcha$getClient$1;
        Object coroutine_suspended;
        int i;
        try {
            if (continuation instanceof Recaptcha$getClient$1) {
                recaptcha$getClient$1 = (Recaptcha$getClient$1) continuation;
                int i2 = recaptcha$getClient$1.zzc;
                if ((i2 & Integer.MIN_VALUE) != 0) {
                    recaptcha$getClient$1.zzc = i2 - Integer.MIN_VALUE;
                    Recaptcha$getClient$1 recaptcha$getClient$12 = recaptcha$getClient$1;
                    Object obj = recaptcha$getClient$12.zza;
                    coroutine_suspended = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
                    i = recaptcha$getClient$12.zzc;
                    if (i != 0) {
                        ResultKt.throwOnFailure(obj);
                        Result.Companion companion = Result.Companion;
                        zzam zzamVar = zzam.zza;
                        recaptcha$getClient$12.zzc = 1;
                        obj = zzam.zzc(application, str, j, null, recaptcha$getClient$12);
                        if (obj == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                    } else {
                        if (i != 1) {
                            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                        }
                        ResultKt.throwOnFailure(obj);
                    }
                    return Result.constructor-impl((zzaw) obj);
                }
            }
            if (i != 0) {
            }
            return Result.constructor-impl((zzaw) obj);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.Companion;
            return Result.constructor-impl(ResultKt.createFailure(th));
        }
        recaptcha$getClient$1 = new Recaptcha$getClient$1(this, continuation);
        Recaptcha$getClient$1 recaptcha$getClient$122 = recaptcha$getClient$1;
        Object obj2 = recaptcha$getClient$122.zza;
        coroutine_suspended = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = recaptcha$getClient$122.zzc;
    }
}
