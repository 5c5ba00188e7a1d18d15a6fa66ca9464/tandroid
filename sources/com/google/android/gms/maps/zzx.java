package com.google.android.gms.maps;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.internal.zzo;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzx extends zzo {
    final /* synthetic */ GoogleMap.OnCameraIdleListener zza;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzx(GoogleMap googleMap, GoogleMap.OnCameraIdleListener onCameraIdleListener) {
        this.zza = onCameraIdleListener;
    }

    @Override // com.google.android.gms.maps.internal.zzp
    public final void zzb() {
        this.zza.onCameraIdle();
    }
}
