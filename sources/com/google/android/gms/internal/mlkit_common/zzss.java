package com.google.android.gms.internal.mlkit_common;

import android.content.Context;
import com.google.android.gms.common.internal.LibraryVersion;
import com.google.android.gms.dynamite.DynamiteModule;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.common.sdkinternal.CommonUtils;
import com.google.mlkit.common.sdkinternal.MLTaskExecutor;
import com.google.mlkit.common.sdkinternal.SharedPrefManager;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/* loaded from: classes.dex */
public final class zzss {
    private static final zzat zzb = zzat.zzc("optional-module-barcode", "com.google.android.gms.vision.barcode");
    private final String zzc;
    private final String zzd;
    private final zzsk zze;
    private final SharedPrefManager zzf;
    private final Task zzg;
    private final Task zzh;
    private final String zzi;
    private final int zzj;
    private final Map zzk = new HashMap();
    private final Map zzl = new HashMap();

    public zzss(Context context, final SharedPrefManager sharedPrefManager, zzsk zzskVar, String str) {
        this.zzc = context.getPackageName();
        this.zzd = CommonUtils.getAppVersion(context);
        this.zzf = sharedPrefManager;
        this.zze = zzskVar;
        zztg.zza();
        this.zzi = str;
        this.zzg = MLTaskExecutor.getInstance().scheduleCallable(new Callable() { // from class: com.google.android.gms.internal.mlkit_common.zzsp
            @Override // java.util.concurrent.Callable
            public final Object call() {
                return zzss.this.zza();
            }
        });
        MLTaskExecutor mLTaskExecutor = MLTaskExecutor.getInstance();
        sharedPrefManager.getClass();
        this.zzh = mLTaskExecutor.scheduleCallable(new Callable() { // from class: com.google.android.gms.internal.mlkit_common.zzsq
            @Override // java.util.concurrent.Callable
            public final Object call() {
                return SharedPrefManager.this.getMlSdkInstanceId();
            }
        });
        zzat zzatVar = zzb;
        this.zzj = zzatVar.containsKey(str) ? DynamiteModule.getRemoteVersion(context, (String) zzatVar.get(str)) : -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ String zza() {
        return LibraryVersion.getInstance().getVersion(this.zzi);
    }
}
