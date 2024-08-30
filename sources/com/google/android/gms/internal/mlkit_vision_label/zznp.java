package com.google.android.gms.internal.mlkit_vision_label;

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
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
/* loaded from: classes.dex */
public final class zznp {
    private static zzbe zza;
    private static final zzbg zzb = zzbg.zzc("optional-module-barcode", "com.google.android.gms.vision.barcode");
    private final String zzc;
    private final String zzd;
    private final zzno zze;
    private final SharedPrefManager zzf;
    private final Task zzg;
    private final Task zzh;
    private final String zzi;
    private final int zzj;
    private final Map zzk = new HashMap();
    private final Map zzl = new HashMap();

    public zznp(Context context, final SharedPrefManager sharedPrefManager, zzno zznoVar, String str) {
        this.zzc = context.getPackageName();
        this.zzd = CommonUtils.getAppVersion(context);
        this.zzf = sharedPrefManager;
        this.zze = zznoVar;
        zzob.zza();
        this.zzi = str;
        this.zzg = MLTaskExecutor.getInstance().scheduleCallable(new Callable() { // from class: com.google.android.gms.internal.mlkit_vision_label.zznj
            @Override // java.util.concurrent.Callable
            public final Object call() {
                return zznp.this.zzb();
            }
        });
        MLTaskExecutor mLTaskExecutor = MLTaskExecutor.getInstance();
        sharedPrefManager.getClass();
        this.zzh = mLTaskExecutor.scheduleCallable(new Callable() { // from class: com.google.android.gms.internal.mlkit_vision_label.zznk
            @Override // java.util.concurrent.Callable
            public final Object call() {
                return SharedPrefManager.this.getMlSdkInstanceId();
            }
        });
        zzbg zzbgVar = zzb;
        this.zzj = zzbgVar.containsKey(str) ? DynamiteModule.getRemoteVersion(context, (String) zzbgVar.get(str)) : -1;
    }

    static long zza(List list, double d) {
        double d2;
        Double.isNaN(list.size());
        return ((Long) list.get(Math.max(((int) Math.ceil((d / 100.0d) * d2)) - 1, 0))).longValue();
    }

    private static synchronized zzbe zzi() {
        synchronized (zznp.class) {
            try {
                zzbe zzbeVar = zza;
                if (zzbeVar != null) {
                    return zzbeVar;
                }
                LocaleListCompat locales = ConfigurationCompat.getLocales(Resources.getSystem().getConfiguration());
                zzbb zzbbVar = new zzbb();
                for (int i = 0; i < locales.size(); i++) {
                    zzbbVar.zzb(CommonUtils.languageTagFromLocale(locales.get(i)));
                }
                zzbe zzc = zzbbVar.zzc();
                zza = zzc;
                return zzc;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private final String zzj() {
        return this.zzg.isSuccessful() ? (String) this.zzg.getResult() : LibraryVersion.getInstance().getVersion(this.zzi);
    }

    private final boolean zzk(zzkf zzkfVar, long j, long j2) {
        return this.zzk.get(zzkfVar) == null || j - ((Long) this.zzk.get(zzkfVar)).longValue() > TimeUnit.SECONDS.toMillis(30L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ String zzb() {
        return LibraryVersion.getInstance().getVersion(this.zzi);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ void zzc(zznf zznfVar, zzkf zzkfVar, String str) {
        zznfVar.zzb(zzkfVar);
        String zzd = zznfVar.zzd();
        zzmh zzmhVar = new zzmh();
        zzmhVar.zzb(this.zzc);
        zzmhVar.zzc(this.zzd);
        zzmhVar.zzh(zzi());
        zzmhVar.zzg(Boolean.TRUE);
        zzmhVar.zzl(zzd);
        zzmhVar.zzj(str);
        zzmhVar.zzi(this.zzh.isSuccessful() ? (String) this.zzh.getResult() : this.zzf.getMlSdkInstanceId());
        zzmhVar.zzd(10);
        zzmhVar.zzk(Integer.valueOf(this.zzj));
        zznfVar.zzc(zzmhVar);
        this.zze.zza(zznfVar);
    }

    public final void zzd(zznf zznfVar, zzkf zzkfVar) {
        zze(zznfVar, zzkfVar, zzj());
    }

    public final void zze(final zznf zznfVar, final zzkf zzkfVar, final String str) {
        MLTaskExecutor.workerThreadExecutor().execute(new Runnable() { // from class: com.google.android.gms.internal.mlkit_vision_label.zznl
            @Override // java.lang.Runnable
            public final void run() {
                zznp.this.zzc(zznfVar, zzkfVar, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ void zzf(zzkf zzkfVar, com.google.mlkit.vision.label.defaults.thin.zzg zzgVar) {
        zzbj zzbjVar = (zzbj) this.zzl.get(zzkfVar);
        if (zzbjVar != null) {
            for (Object obj : zzbjVar.zzq()) {
                ArrayList<Long> arrayList = new ArrayList(zzbjVar.zzc(obj));
                Collections.sort(arrayList);
                zzjl zzjlVar = new zzjl();
                long j = 0;
                for (Long l : arrayList) {
                    j += l.longValue();
                }
                zzjlVar.zza(Long.valueOf(j / arrayList.size()));
                zzjlVar.zzc(Long.valueOf(zza(arrayList, 100.0d)));
                zzjlVar.zzf(Long.valueOf(zza(arrayList, 75.0d)));
                zzjlVar.zzd(Long.valueOf(zza(arrayList, 50.0d)));
                zzjlVar.zzb(Long.valueOf(zza(arrayList, 25.0d)));
                zzjlVar.zze(Long.valueOf(zza(arrayList, 0.0d)));
                zzjn zzg = zzjlVar.zzg();
                int size = arrayList.size();
                zzkg zzkgVar = new zzkg();
                zzkgVar.zze(zzkd.zzb);
                zzde zzdeVar = new zzde();
                zzdeVar.zza(Integer.valueOf(size));
                zzdeVar.zzc((zzdh) obj);
                zzdeVar.zzb(zzg);
                zzkgVar.zzd(zzdeVar.zze());
                zze(zzns.zzf(zzkgVar), zzkfVar, zzj());
            }
            this.zzl.remove(zzkfVar);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ void zzg(final zzkf zzkfVar, Object obj, long j, final com.google.mlkit.vision.label.defaults.thin.zzg zzgVar) {
        if (!this.zzl.containsKey(zzkfVar)) {
            this.zzl.put(zzkfVar, zzaj.zzr());
        }
        ((zzbj) this.zzl.get(zzkfVar)).zzo(obj, Long.valueOf(j));
        long elapsedRealtime = SystemClock.elapsedRealtime();
        if (zzk(zzkfVar, elapsedRealtime, 30L)) {
            this.zzk.put(zzkfVar, Long.valueOf(elapsedRealtime));
            MLTaskExecutor.workerThreadExecutor().execute(new Runnable(zzkfVar, zzgVar, null) { // from class: com.google.android.gms.internal.mlkit_vision_label.zznn
                public final /* synthetic */ zzkf zzb;
                public final /* synthetic */ com.google.mlkit.vision.label.defaults.thin.zzg zzc;

                @Override // java.lang.Runnable
                public final void run() {
                    zznp.this.zzf(this.zzb, this.zzc);
                }
            });
        }
    }

    public final void zzh(com.google.mlkit.vision.label.defaults.thin.zzf zzfVar, zzkf zzkfVar) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        if (zzk(zzkfVar, elapsedRealtime, 30L)) {
            this.zzk.put(zzkfVar, Long.valueOf(elapsedRealtime));
            zze(zzfVar.zza(), zzkfVar, zzj());
        }
    }
}
