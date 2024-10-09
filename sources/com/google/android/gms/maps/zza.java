package com.google.android.gms.maps;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.internal.zzat;
import com.google.android.gms.maps.model.Marker;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zza extends zzat {
    final /* synthetic */ GoogleMap.OnMarkerClickListener zza;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zza(GoogleMap googleMap, GoogleMap.OnMarkerClickListener onMarkerClickListener) {
        this.zza = onMarkerClickListener;
    }

    @Override // com.google.android.gms.maps.internal.zzau
    public final boolean zzb(com.google.android.gms.internal.maps.zzaa zzaaVar) {
        return this.zza.onMarkerClick(new Marker(zzaaVar));
    }
}
