package com.google.android.gms.maps.internal;

import android.os.IInterface;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.internal.maps.zzaa;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
/* compiled from: com.google.android.gms:play-services-maps@@18.1.0 */
/* loaded from: classes.dex */
public interface IGoogleMapDelegate extends IInterface {
    com.google.android.gms.internal.maps.zzl addCircle(CircleOptions circleOptions) throws RemoteException;

    zzaa addMarker(MarkerOptions markerOptions) throws RemoteException;

    void animateCamera(IObjectWrapper iObjectWrapper) throws RemoteException;

    void animateCameraWithCallback(IObjectWrapper iObjectWrapper, zzd zzdVar) throws RemoteException;

    void animateCameraWithDurationAndCallback(IObjectWrapper iObjectWrapper, int i, zzd zzdVar) throws RemoteException;

    CameraPosition getCameraPosition() throws RemoteException;

    float getMaxZoomLevel() throws RemoteException;

    IProjectionDelegate getProjection() throws RemoteException;

    IUiSettingsDelegate getUiSettings() throws RemoteException;

    void moveCamera(IObjectWrapper iObjectWrapper) throws RemoteException;

    boolean setMapStyle(MapStyleOptions mapStyleOptions) throws RemoteException;

    void setMapType(int i) throws RemoteException;

    void setMyLocationEnabled(boolean z) throws RemoteException;

    void setOnCameraMoveListener(zzt zztVar) throws RemoteException;

    void setOnCameraMoveStartedListener(zzv zzvVar) throws RemoteException;

    void setOnMapLoadedCallback(zzao zzaoVar) throws RemoteException;

    void setOnMarkerClickListener(zzau zzauVar) throws RemoteException;

    void setOnMyLocationChangeListener(zzba zzbaVar) throws RemoteException;

    void setPadding(int i, int i2, int i3, int i4) throws RemoteException;
}
