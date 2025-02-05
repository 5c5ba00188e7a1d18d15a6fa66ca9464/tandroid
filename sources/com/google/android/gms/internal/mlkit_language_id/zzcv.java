package com.google.android.gms.internal.mlkit_language_id;

import android.content.Context;
import android.content.res.Resources;
import android.os.SystemClock;
import androidx.core.os.ConfigurationCompat;
import androidx.core.os.LocaleListCompat;
import com.google.android.gms.common.internal.LibraryVersion;
import com.google.android.gms.internal.mlkit_language_id.zzy$zzad;
import com.google.android.gms.internal.mlkit_language_id.zzy$zzbh;
import com.google.android.gms.tasks.Task;
import com.google.firebase.components.Component;
import com.google.firebase.components.ComponentContainer;
import com.google.firebase.components.Dependency;
import com.google.mlkit.common.sdkinternal.CommonUtils;
import com.google.mlkit.common.sdkinternal.MLTaskExecutor;
import com.google.mlkit.common.sdkinternal.SharedPrefManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/* loaded from: classes.dex */
public final class zzcv {
    public static final Component zza = Component.builder(zzcv.class).add(Dependency.required(Context.class)).add(Dependency.required(SharedPrefManager.class)).add(Dependency.required(zzb.class)).factory(zzcy.zza).build();
    private static List zzb = null;
    private static boolean zzl = true;
    private final String zzc;
    private final String zzd;
    private final zzb zze;
    private final SharedPrefManager zzf;
    private final Task zzh;
    private final Map zzi = new HashMap();
    private final Map zzj = new HashMap();
    private final Task zzg = MLTaskExecutor.getInstance().scheduleCallable(zzcu.zza);

    public interface zza {
        zzy$zzad.zza zza();
    }

    public interface zzb {
        void zza(zzy$zzad zzy_zzad);
    }

    private zzcv(Context context, SharedPrefManager sharedPrefManager, zzb zzbVar) {
        this.zzc = context.getPackageName();
        this.zzd = CommonUtils.getAppVersion(context);
        this.zzf = sharedPrefManager;
        this.zze = zzbVar;
        MLTaskExecutor mLTaskExecutor = MLTaskExecutor.getInstance();
        sharedPrefManager.getClass();
        this.zzh = mLTaskExecutor.scheduleCallable(zzcx.zza(sharedPrefManager));
    }

    static final /* synthetic */ zzcv zza(ComponentContainer componentContainer) {
        return new zzcv((Context) componentContainer.get(Context.class), (SharedPrefManager) componentContainer.get(SharedPrefManager.class), (zzb) componentContainer.get(zzb.class));
    }

    private static synchronized List zzb() {
        synchronized (zzcv.class) {
            try {
                List list = zzb;
                if (list != null) {
                    return list;
                }
                LocaleListCompat locales = ConfigurationCompat.getLocales(Resources.getSystem().getConfiguration());
                zzb = new ArrayList(locales.size());
                for (int i = 0; i < locales.size(); i++) {
                    zzb.add(CommonUtils.languageTagFromLocale(locales.get(i)));
                }
                return zzb;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public final void zza(zza zzaVar, zzaj zzajVar) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        if (this.zzi.get(zzajVar) != null && elapsedRealtime - ((Long) this.zzi.get(zzajVar)).longValue() <= TimeUnit.SECONDS.toMillis(30L)) {
            return;
        }
        this.zzi.put(zzajVar, Long.valueOf(elapsedRealtime));
        zza(zzaVar.zza(), zzajVar);
    }

    public final void zza(final zzy$zzad.zza zzaVar, final zzaj zzajVar) {
        MLTaskExecutor.workerThreadExecutor().execute(new Runnable(this, zzaVar, zzajVar) { // from class: com.google.android.gms.internal.mlkit_language_id.zzcw
            private final zzcv zza;
            private final zzy$zzad.zza zzb;
            private final zzaj zzc;

            {
                this.zza = this;
                this.zzb = zzaVar;
                this.zzc = zzajVar;
            }

            @Override // java.lang.Runnable
            public final void run() {
                this.zza.zzb(this.zzb, this.zzc);
            }
        });
    }

    final /* synthetic */ void zzb(zzy$zzad.zza zzaVar, zzaj zzajVar) {
        String zza2 = zzaVar.zza().zza();
        if ("NA".equals(zza2) || "".equals(zza2)) {
            zza2 = "NA";
        }
        zzy$zzbh.zza zzc = zzy$zzbh.zzb().zza(this.zzc).zzb(this.zzd).zzd(zza2).zza(zzb()).zzb(true).zzc(this.zzg.isSuccessful() ? (String) this.zzg.getResult() : LibraryVersion.getInstance().getVersion("language-id"));
        if (zzl) {
            zzc.zze(this.zzh.isSuccessful() ? (String) this.zzh.getResult() : this.zzf.getMlSdkInstanceId());
        }
        zzaVar.zza(zzajVar).zza(zzc);
        this.zze.zza((zzy$zzad) ((zzeo) zzaVar.zzg()));
    }
}
