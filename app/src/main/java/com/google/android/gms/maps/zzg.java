package com.google.android.gms.maps;

import android.location.Location;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.dynamic.ObjectWrapper;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.internal.zzay;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-maps@@17.0.1 */
/* loaded from: classes.dex */
public final class zzg extends zzay {
    final /* synthetic */ GoogleMap.OnMyLocationChangeListener zza;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzg(GoogleMap googleMap, GoogleMap.OnMyLocationChangeListener onMyLocationChangeListener) {
        this.zza = onMyLocationChangeListener;
    }

    @Override // com.google.android.gms.maps.internal.zzaz
    public final void zzb(IObjectWrapper iObjectWrapper) {
        this.zza.onMyLocationChange((Location) ObjectWrapper.unwrap(iObjectWrapper));
    }
}
