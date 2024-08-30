package com.google.android.gms.maps.internal;

import android.os.IInterface;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.internal.maps.zzaa;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
/* loaded from: classes.dex */
public interface IGoogleMapDelegate extends IInterface {
    com.google.android.gms.internal.maps.zzl addCircle(CircleOptions circleOptions);

    zzaa addMarker(MarkerOptions markerOptions);

    void animateCamera(IObjectWrapper iObjectWrapper);

    void animateCameraWithCallback(IObjectWrapper iObjectWrapper, zzd zzdVar);

    void animateCameraWithDurationAndCallback(IObjectWrapper iObjectWrapper, int i, zzd zzdVar);

    CameraPosition getCameraPosition();

    float getMaxZoomLevel();

    float getMinZoomLevel();

    IProjectionDelegate getProjection();

    IUiSettingsDelegate getUiSettings();

    void moveCamera(IObjectWrapper iObjectWrapper);

    boolean setMapStyle(MapStyleOptions mapStyleOptions);

    void setMapType(int i);

    void setMyLocationEnabled(boolean z);

    void setOnCameraIdleListener(zzp zzpVar);

    void setOnCameraMoveListener(zzt zztVar);

    void setOnCameraMoveStartedListener(zzv zzvVar);

    void setOnMapLoadedCallback(zzao zzaoVar);

    void setOnMarkerClickListener(zzau zzauVar);

    void setOnMyLocationChangeListener(zzba zzbaVar);

    void setPadding(int i, int i2, int i3, int i4);
}
