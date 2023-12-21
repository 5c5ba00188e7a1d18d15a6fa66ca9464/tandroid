package com.google.android.gms.internal.mlkit_common;

import android.content.Context;
import com.google.android.gms.common.internal.LibraryVersion;
import com.google.android.gms.dynamite.DynamiteModule;
import com.google.mlkit.common.sdkinternal.CommonUtils;
import com.google.mlkit.common.sdkinternal.MLTaskExecutor;
import com.google.mlkit.common.sdkinternal.SharedPrefManager;
import java.util.HashMap;
import java.util.concurrent.Callable;
/* compiled from: com.google.mlkit:common@@18.10.0 */
/* loaded from: classes.dex */
public final class zzss {
    private static final zzat zzb = zzat.zzc("optional-module-barcode", "com.google.android.gms.vision.barcode");
    private final String zzi;

    public zzss(Context context, final SharedPrefManager sharedPrefManager, zzsk zzskVar, String str) {
        new HashMap();
        new HashMap();
        context.getPackageName();
        CommonUtils.getAppVersion(context);
        zztg.zza();
        this.zzi = str;
        MLTaskExecutor.getInstance().scheduleCallable(new Callable() { // from class: com.google.android.gms.internal.mlkit_common.zzsp
            @Override // java.util.concurrent.Callable
            public final Object call() {
                return zzss.this.zza();
            }
        });
        MLTaskExecutor mLTaskExecutor = MLTaskExecutor.getInstance();
        sharedPrefManager.getClass();
        mLTaskExecutor.scheduleCallable(new Callable() { // from class: com.google.android.gms.internal.mlkit_common.zzsq
            @Override // java.util.concurrent.Callable
            public final Object call() {
                return SharedPrefManager.this.getMlSdkInstanceId();
            }
        });
        zzat zzatVar = zzb;
        if (zzatVar.containsKey(str)) {
            DynamiteModule.getRemoteVersion(context, (String) zzatVar.get(str));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ String zza() throws Exception {
        return LibraryVersion.getInstance().getVersion(this.zzi);
    }
}
