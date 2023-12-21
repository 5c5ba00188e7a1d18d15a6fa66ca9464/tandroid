package com.google.mlkit.vision.common.internal;

import com.google.android.gms.tasks.OnFailureListener;
/* compiled from: com.google.mlkit:vision-common@@17.3.0 */
/* loaded from: classes.dex */
public final /* synthetic */ class zzc implements OnFailureListener {
    public static final /* synthetic */ zzc zza = new zzc();

    private /* synthetic */ zzc() {
    }

    @Override // com.google.android.gms.tasks.OnFailureListener
    public final void onFailure(Exception exc) {
        MobileVisionBase.zzb.e("MobileVisionBase", "Error preloading model resource", exc);
    }
}
