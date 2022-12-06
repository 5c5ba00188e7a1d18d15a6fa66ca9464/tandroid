package com.google.android.gms.maps.internal;

import android.os.IInterface;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.internal.maps.zzaa;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import javax.annotation.Nullable;
/* compiled from: com.google.android.gms:play-services-maps@@18.1.0 */
/* loaded from: classes.dex */
public interface IGoogleMapDelegate extends IInterface {
    com.google.android.gms.internal.maps.zzl addCircle(CircleOptions circleOptions) throws RemoteException;

    zzaa addMarker(MarkerOptions markerOptions) throws RemoteException;

    void animateCamera(IObjectWrapper iObjectWrapper) throws RemoteException;

    void animateCameraWithCallback(IObjectWrapper iObjectWrapper, @Nullable zzd zzdVar) throws RemoteException;

    void animateCameraWithDurationAndCallback(IObjectWrapper iObjectWrapper, int i, @Nullable zzd zzdVar) throws RemoteException;

    CameraPosition getCameraPosition() throws RemoteException;

    float getMaxZoomLevel() throws RemoteException;

    IProjectionDelegate getProjection() throws RemoteException;

    IUiSettingsDelegate getUiSettings() throws RemoteException;

    void moveCamera(IObjectWrapper iObjectWrapper) throws RemoteException;

    boolean setMapStyle(@Nullable MapStyleOptions mapStyleOptions) throws RemoteException;

    void setMapType(int i) throws RemoteException;

    void setMyLocationEnabled(boolean z) throws RemoteException;

    void setOnCameraMoveListener(@Nullable zzt zztVar) throws RemoteException;

    void setOnCameraMoveStartedListener(@Nullable zzv zzvVar) throws RemoteException;

    void setOnMapLoadedCallback(@Nullable zzao zzaoVar) throws RemoteException;

    void setOnMarkerClickListener(@Nullable zzau zzauVar) throws RemoteException;

    void setOnMyLocationChangeListener(@Nullable zzba zzbaVar) throws RemoteException;

    void setPadding(int i, int i2, int i3, int i4) throws RemoteException;
}
