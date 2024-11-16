package com.google.android.gms.internal.mlkit_vision_subject_segmentation;

import android.content.Context;
import android.content.res.Resources;
import android.os.SystemClock;
import androidx.core.os.ConfigurationCompat;
import androidx.core.os.LocaleListCompat;
import com.google.android.gms.common.internal.LibraryVersion;
import com.google.android.gms.dynamite.DynamiteModule;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.common.sdkinternal.CommonUtils;
import com.google.mlkit.common.sdkinternal.MLTaskExecutor;
import com.google.mlkit.common.sdkinternal.SharedPrefManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/* loaded from: classes.dex */
public final class zztl {
    private static zzav zza;
    private static final zzax zzb = zzax.zzc("optional-module-barcode", "com.google.android.gms.vision.barcode");
    private final String zzc;
    private final String zzd;
    private final zztb zze;
    private final SharedPrefManager zzf;
    private final Task zzg;
    private final Task zzh;
    private final String zzi;
    private final int zzj;
    private final Map zzk = new HashMap();
    private final Map zzl = new HashMap();

    public zztl(Context context, final SharedPrefManager sharedPrefManager, zztb zztbVar, String str) {
        this.zzc = context.getPackageName();
        this.zzd = CommonUtils.getAppVersion(context);
        this.zzf = sharedPrefManager;
        this.zze = zztbVar;
        zztx.zza();
        this.zzi = str;
        this.zzg = MLTaskExecutor.getInstance().scheduleCallable(new Callable() { // from class: com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzth
            @Override // java.util.concurrent.Callable
            public final Object call() {
                return zztl.this.zzb();
            }
        });
        MLTaskExecutor mLTaskExecutor = MLTaskExecutor.getInstance();
        sharedPrefManager.getClass();
        this.zzh = mLTaskExecutor.scheduleCallable(new Callable() { // from class: com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzti
            @Override // java.util.concurrent.Callable
            public final Object call() {
                return SharedPrefManager.this.getMlSdkInstanceId();
            }
        });
        zzax zzaxVar = zzb;
        this.zzj = zzaxVar.containsKey(str) ? DynamiteModule.getRemoteVersion(context, (String) zzaxVar.get(str)) : -1;
    }

    static long zza(List list, double d) {
        Double.isNaN(list.size());
        return ((Long) list.get(Math.max(((int) Math.ceil((d / 100.0d) * r0)) - 1, 0))).longValue();
    }

    private static synchronized zzav zzi() {
        synchronized (zztl.class) {
            try {
                zzav zzavVar = zza;
                if (zzavVar != null) {
                    return zzavVar;
                }
                LocaleListCompat locales = ConfigurationCompat.getLocales(Resources.getSystem().getConfiguration());
                zzas zzasVar = new zzas();
                for (int i = 0; i < locales.size(); i++) {
                    zzasVar.zza(CommonUtils.languageTagFromLocale(locales.get(i)));
                }
                zzav zzb2 = zzasVar.zzb();
                zza = zzb2;
                return zzb2;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private final String zzj() {
        if (this.zzg.isSuccessful()) {
            return (String) this.zzg.getResult();
        }
        return LibraryVersion.getInstance().getVersion(this.zzi);
    }

    private final boolean zzk(zzob zzobVar, long j, long j2) {
        return this.zzk.get(zzobVar) == null || j - ((Long) this.zzk.get(zzobVar)).longValue() > TimeUnit.SECONDS.toMillis(30L);
    }

    final /* synthetic */ String zzb() {
        return LibraryVersion.getInstance().getVersion(this.zzi);
    }

    final /* synthetic */ void zzc(zzta zztaVar, zzob zzobVar, String str) {
        zztaVar.zzb(zzobVar);
        String zzd = zztaVar.zzd();
        zzsa zzsaVar = new zzsa();
        zzsaVar.zzb(this.zzc);
        zzsaVar.zzc(this.zzd);
        zzsaVar.zzh(zzi());
        zzsaVar.zzg(Boolean.TRUE);
        zzsaVar.zzl(zzd);
        zzsaVar.zzj(str);
        zzsaVar.zzi(this.zzh.isSuccessful() ? (String) this.zzh.getResult() : this.zzf.getMlSdkInstanceId());
        zzsaVar.zzd(10);
        zzsaVar.zzk(Integer.valueOf(this.zzj));
        zztaVar.zzc(zzsaVar);
        this.zze.zza(zztaVar);
    }

    public final void zzd(zzta zztaVar, zzob zzobVar) {
        zze(zztaVar, zzobVar, zzj());
    }

    public final void zze(final zzta zztaVar, final zzob zzobVar, final String str) {
        MLTaskExecutor.workerThreadExecutor().execute(new Runnable() { // from class: com.google.android.gms.internal.mlkit_vision_subject_segmentation.zztf
            @Override // java.lang.Runnable
            public final void run() {
                zztl.this.zzc(zztaVar, zzobVar, str);
            }
        });
    }

    public final void zzf(zztk zztkVar, zzob zzobVar) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        if (zzk(zzobVar, elapsedRealtime, 30L)) {
            this.zzk.put(zzobVar, Long.valueOf(elapsedRealtime));
            zze(zztkVar.zza(), zzobVar, zzj());
        }
    }

    final /* synthetic */ void zzg(zzob zzobVar, com.google.mlkit.vision.segmentation.subject.internal.zzh zzhVar) {
        zzba zzbaVar = (zzba) this.zzl.get(zzobVar);
        if (zzbaVar != null) {
            for (Object obj : zzbaVar.zzo()) {
                ArrayList arrayList = new ArrayList(zzbaVar.zzc(obj));
                Collections.sort(arrayList);
                zzna zznaVar = new zzna();
                Iterator it = arrayList.iterator();
                long j = 0;
                while (it.hasNext()) {
                    j += ((Long) it.next()).longValue();
                }
                zznaVar.zza(Long.valueOf(j / arrayList.size()));
                zznaVar.zzc(Long.valueOf(zza(arrayList, 100.0d)));
                zznaVar.zzf(Long.valueOf(zza(arrayList, 75.0d)));
                zznaVar.zzd(Long.valueOf(zza(arrayList, 50.0d)));
                zznaVar.zzb(Long.valueOf(zza(arrayList, 25.0d)));
                zznaVar.zze(Long.valueOf(zza(arrayList, 0.0d)));
                zznc zzg = zznaVar.zzg();
                int size = arrayList.size();
                zzoc zzocVar = new zzoc();
                zzocVar.zze(zznz.zzb);
                zzdt zzdtVar = new zzdt();
                zzdtVar.zza(Integer.valueOf(size));
                zzdtVar.zzc((zzdw) obj);
                zzdtVar.zzb(zzg);
                zzocVar.zzd(zzdtVar.zze());
                zze(zzto.zzf(zzocVar), zzobVar, zzj());
            }
            this.zzl.remove(zzobVar);
        }
    }

    final /* synthetic */ void zzh(final zzob zzobVar, Object obj, long j, final com.google.mlkit.vision.segmentation.subject.internal.zzh zzhVar) {
        if (!this.zzl.containsKey(zzobVar)) {
            this.zzl.put(zzobVar, zzz.zzp());
        }
        ((zzba) this.zzl.get(zzobVar)).zzm(obj, Long.valueOf(j));
        long elapsedRealtime = SystemClock.elapsedRealtime();
        if (zzk(zzobVar, elapsedRealtime, 30L)) {
            this.zzk.put(zzobVar, Long.valueOf(elapsedRealtime));
            MLTaskExecutor.workerThreadExecutor().execute(new Runnable() { // from class: com.google.android.gms.internal.mlkit_vision_subject_segmentation.zztg
                @Override // java.lang.Runnable
                public final void run() {
                    zztl.this.zzg(zzobVar, zzhVar);
                }
            });
        }
    }
}
