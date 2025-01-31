package com.google.android.recaptcha.internal;

import com.google.android.recaptcha.RecaptchaErrorCode;
import com.google.android.recaptcha.RecaptchaException;
import java.util.Map;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.collections.MapsKt__MapsKt;
import kotlin.jvm.internal.Intrinsics;

/* loaded from: classes.dex */
public final class zzp extends Exception {
    public static final zzo zza = new zzo(null);
    private static final Map zzb;
    private final zzn zzc;
    private final zzl zzd;
    private final String zze;
    private final Map zzf;

    static {
        Map mapOf;
        Pair pair = TuplesKt.to(zzpb.zzc, new zzp(zzn.zze, zzl.zzm, null));
        zzpb zzpbVar = zzpb.zzd;
        zzn zznVar = zzn.zzc;
        mapOf = MapsKt__MapsKt.mapOf(pair, TuplesKt.to(zzpbVar, new zzp(zznVar, zzl.zzk, null)), TuplesKt.to(zzpb.zze, new zzp(zzn.zzf, zzl.zzn, null)), TuplesKt.to(zzpb.zzf, new zzp(zzn.zzg, zzl.zzo, null)), TuplesKt.to(zzpb.zzi, new zzp(zzn.zzh, zzl.zzp, null)), TuplesKt.to(zzpb.zzh, new zzp(zzn.zzi, zzl.zzq, null)), TuplesKt.to(zzpb.zzj, new zzp(zznVar, zzl.zzu, null)));
        zzb = mapOf;
    }

    public zzp(zzn zznVar, zzl zzlVar, String str) {
        Map mapOf;
        this.zzc = zznVar;
        this.zzd = zzlVar;
        this.zze = str;
        zzn zznVar2 = zzn.zze;
        RecaptchaErrorCode recaptchaErrorCode = RecaptchaErrorCode.NETWORK_ERROR;
        mapOf = MapsKt__MapsKt.mapOf(TuplesKt.to(zznVar2, new RecaptchaException(recaptchaErrorCode, null, 2, null)), TuplesKt.to(zzn.zzk, new RecaptchaException(recaptchaErrorCode, null, 2, null)), TuplesKt.to(zzn.zzf, new RecaptchaException(RecaptchaErrorCode.INVALID_SITEKEY, null, 2, null)), TuplesKt.to(zzn.zzg, new RecaptchaException(RecaptchaErrorCode.INVALID_KEYTYPE, null, 2, null)), TuplesKt.to(zzn.zzh, new RecaptchaException(RecaptchaErrorCode.INVALID_PACKAGE_NAME, null, 2, null)), TuplesKt.to(zzn.zzi, new RecaptchaException(RecaptchaErrorCode.INVALID_ACTION, null, 2, null)), TuplesKt.to(zzn.zzc, new RecaptchaException(RecaptchaErrorCode.INTERNAL_ERROR, null, 2, null)));
        this.zzf = mapOf;
    }

    public final zzl zza() {
        return this.zzd;
    }

    public final zzn zzb() {
        return this.zzc;
    }

    public final RecaptchaException zzc() {
        if (Intrinsics.areEqual(this.zzd, zzl.zzT)) {
            return new RecaptchaException(RecaptchaErrorCode.INVALID_TIMEOUT, null, 2, null);
        }
        RecaptchaException recaptchaException = (RecaptchaException) this.zzf.get(this.zzc);
        return recaptchaException == null ? new RecaptchaException(RecaptchaErrorCode.INTERNAL_ERROR, null, 2, null) : recaptchaException;
    }

    public final String zzd() {
        return this.zze;
    }
}
