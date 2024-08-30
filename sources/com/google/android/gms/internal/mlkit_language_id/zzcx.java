package com.google.android.gms.internal.mlkit_language_id;

import com.google.mlkit.common.sdkinternal.SharedPrefManager;
import java.util.concurrent.Callable;
/* loaded from: classes.dex */
final /* synthetic */ class zzcx implements Callable {
    private final SharedPrefManager zza;

    private zzcx(SharedPrefManager sharedPrefManager) {
        this.zza = sharedPrefManager;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Callable zza(SharedPrefManager sharedPrefManager) {
        return new zzcx(sharedPrefManager);
    }

    @Override // java.util.concurrent.Callable
    public final Object call() {
        return this.zza.getMlSdkInstanceId();
    }
}
