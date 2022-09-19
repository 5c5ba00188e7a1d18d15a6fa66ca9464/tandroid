package com.google.android.gms.maps;

import com.google.android.gms.internal.maps.zzx;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.internal.zzas;
import com.google.android.gms.maps.model.Marker;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-maps@@17.0.1 */
/* loaded from: classes.dex */
public final class zza extends zzas {
    final /* synthetic */ GoogleMap.OnMarkerClickListener zza;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zza(GoogleMap googleMap, GoogleMap.OnMarkerClickListener onMarkerClickListener) {
        this.zza = onMarkerClickListener;
    }

    @Override // com.google.android.gms.maps.internal.zzat
    public final boolean zzb(zzx zzxVar) {
        return this.zza.onMarkerClick(new Marker(zzxVar));
    }
}
