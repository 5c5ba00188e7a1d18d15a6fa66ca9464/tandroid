package com.google.android.gms.maps;

import android.os.RemoteException;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.RuntimeRemoteException;

/* loaded from: classes.dex */
public abstract class CameraUpdateFactory {
    private static ICameraUpdateFactoryDelegate zza;

    public static CameraUpdate newLatLng(LatLng latLng) {
        Preconditions.checkNotNull(latLng, "latLng must not be null");
        try {
            return new CameraUpdate(zzb().newLatLng(latLng));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public static CameraUpdate newLatLngBounds(LatLngBounds latLngBounds, int i) {
        Preconditions.checkNotNull(latLngBounds, "bounds must not be null");
        try {
            return new CameraUpdate(zzb().newLatLngBounds(latLngBounds, i));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public static CameraUpdate newLatLngZoom(LatLng latLng, float f) {
        Preconditions.checkNotNull(latLng, "latLng must not be null");
        try {
            return new CameraUpdate(zzb().newLatLngZoom(latLng, f));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public static void zza(ICameraUpdateFactoryDelegate iCameraUpdateFactoryDelegate) {
        zza = (ICameraUpdateFactoryDelegate) Preconditions.checkNotNull(iCameraUpdateFactoryDelegate);
    }

    private static ICameraUpdateFactoryDelegate zzb() {
        return (ICameraUpdateFactoryDelegate) Preconditions.checkNotNull(zza, "CameraUpdateFactory is not initialized");
    }
}
