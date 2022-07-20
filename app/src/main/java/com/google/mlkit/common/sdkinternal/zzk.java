package com.google.mlkit.common.sdkinternal;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.mlkit:common@@17.0.0 */
/* loaded from: classes.dex */
public final /* synthetic */ class zzk implements Runnable {
    private final ModelResource zza;

    public zzk(ModelResource modelResource) {
        this.zza = modelResource;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zza.zza();
    }
}
