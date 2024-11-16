package com.google.android.gms.maps.internal;

import android.os.IBinder;
import android.os.Parcel;

/* loaded from: classes.dex */
public final class zzbz extends com.google.android.gms.internal.maps.zza implements IUiSettingsDelegate {
    zzbz(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.maps.internal.IUiSettingsDelegate");
    }

    @Override // com.google.android.gms.maps.internal.IUiSettingsDelegate
    public final void setCompassEnabled(boolean z) {
        Parcel zza = zza();
        com.google.android.gms.internal.maps.zzc.zzd(zza, z);
        zzc(2, zza);
    }

    @Override // com.google.android.gms.maps.internal.IUiSettingsDelegate
    public final void setMyLocationButtonEnabled(boolean z) {
        Parcel zza = zza();
        com.google.android.gms.internal.maps.zzc.zzd(zza, z);
        zzc(3, zza);
    }

    @Override // com.google.android.gms.maps.internal.IUiSettingsDelegate
    public final void setZoomControlsEnabled(boolean z) {
        Parcel zza = zza();
        com.google.android.gms.internal.maps.zzc.zzd(zza, z);
        zzc(1, zza);
    }
}
