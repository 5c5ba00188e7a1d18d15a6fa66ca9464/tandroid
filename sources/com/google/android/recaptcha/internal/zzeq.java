package com.google.android.recaptcha.internal;

import android.webkit.JavascriptInterface;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import kotlin.Unit;
import kotlinx.coroutines.CompletableDeferred;

/* loaded from: classes.dex */
public final class zzeq {
    final /* synthetic */ zzez zza;
    private Long zzb;
    private final zzfh zzc = zzfh.zzb();

    public zzeq(zzez zzezVar) {
        this.zza = zzezVar;
    }

    private final void zzb() {
        if (this.zzb == null) {
            this.zzc.zzf();
            this.zzb = Long.valueOf(this.zzc.zza(TimeUnit.MILLISECONDS));
        }
    }

    public final Long zza() {
        return this.zzb;
    }

    @JavascriptInterface
    public final void zzlce(String str) {
        zzbg zzbgVar;
        zzbg zzbgVar2;
        zzbd zzbdVar;
        zznf zzI = zznf.zzI(zzfy.zzh().zzj(str));
        zzez zzezVar = this.zza;
        if (zzezVar.zzg().zzb == null) {
            zzbgVar2 = zzezVar.zzi;
            zzbdVar = zzezVar.zzp;
            zzbgVar2.zza(zzbdVar.zza(zzne.zzl));
        }
        zzb();
        zzpc zzi = zzpd.zzi();
        zzi.zzd(zzI);
        zzpd zzpdVar = (zzpd) zzi.zzj();
        zzbgVar = this.zza.zzi;
        zzbgVar.zzd(zzpdVar);
    }

    @JavascriptInterface
    public final void zzlsm(String str) {
        zzbg zzbgVar;
        zzb();
        zzpc zzi = zzpd.zzi();
        zzi.zze(zznu.zzi(zzfy.zzh().zzj(str)));
        zzpd zzpdVar = (zzpd) zzi.zzj();
        zzbgVar = this.zza.zzi;
        zzbgVar.zzd(zzpdVar);
    }

    @JavascriptInterface
    public final void zzoid(String str) {
        zzb();
        zzox zzg = zzox.zzg(zzfy.zzh().zzj(str));
        zzg.zzi().name();
        if (zzg.zzi() == zzpb.zzb) {
            this.zza.zzk().hashCode();
            if (this.zza.zzk().complete(Unit.INSTANCE)) {
                return;
            }
            this.zza.zzk().hashCode();
            return;
        }
        zzg.zzi().name();
        zzo zzoVar = zzp.zza;
        zzp zza = zzo.zza(zzg.zzi());
        this.zza.zzk().hashCode();
        this.zza.zzk().completeExceptionally(zza);
    }

    @JavascriptInterface
    public final void zzrp(String str) {
        zzb();
        zzbu zzbuVar = this.zza.zzc;
        if (zzbuVar == null) {
            zzbuVar = null;
        }
        zzbuVar.zza(str);
    }

    @JavascriptInterface
    public final void zzscd(String str) {
        Map map;
        zzb();
        zzog zzi = zzog.zzi(zzfy.zzh().zzj(str));
        zzi.toString();
        map = this.zza.zzl;
        CompletableDeferred completableDeferred = (CompletableDeferred) map.remove(zzi.zzk());
        if (completableDeferred != null) {
            completableDeferred.complete(zzi);
        }
    }
}
