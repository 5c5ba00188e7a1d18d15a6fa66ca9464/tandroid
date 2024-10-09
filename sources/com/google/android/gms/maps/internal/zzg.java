package com.google.android.gms.maps.internal;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.internal.maps.zzaa;
import com.google.android.gms.internal.maps.zzk;
import com.google.android.gms.internal.maps.zzz;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

/* loaded from: classes.dex */
public final class zzg extends com.google.android.gms.internal.maps.zza implements IGoogleMapDelegate {
    /* JADX INFO: Access modifiers changed from: package-private */
    public zzg(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.maps.internal.IGoogleMapDelegate");
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final com.google.android.gms.internal.maps.zzl addCircle(CircleOptions circleOptions) {
        Parcel zza = zza();
        com.google.android.gms.internal.maps.zzc.zze(zza, circleOptions);
        Parcel zzH = zzH(35, zza);
        com.google.android.gms.internal.maps.zzl zzb = zzk.zzb(zzH.readStrongBinder());
        zzH.recycle();
        return zzb;
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final zzaa addMarker(MarkerOptions markerOptions) {
        Parcel zza = zza();
        com.google.android.gms.internal.maps.zzc.zze(zza, markerOptions);
        Parcel zzH = zzH(11, zza);
        zzaa zzb = zzz.zzb(zzH.readStrongBinder());
        zzH.recycle();
        return zzb;
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void animateCamera(IObjectWrapper iObjectWrapper) {
        Parcel zza = zza();
        com.google.android.gms.internal.maps.zzc.zzg(zza, iObjectWrapper);
        zzc(5, zza);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void animateCameraWithCallback(IObjectWrapper iObjectWrapper, zzd zzdVar) {
        Parcel zza = zza();
        com.google.android.gms.internal.maps.zzc.zzg(zza, iObjectWrapper);
        com.google.android.gms.internal.maps.zzc.zzg(zza, zzdVar);
        zzc(6, zza);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void animateCameraWithDurationAndCallback(IObjectWrapper iObjectWrapper, int i, zzd zzdVar) {
        Parcel zza = zza();
        com.google.android.gms.internal.maps.zzc.zzg(zza, iObjectWrapper);
        zza.writeInt(i);
        com.google.android.gms.internal.maps.zzc.zzg(zza, zzdVar);
        zzc(7, zza);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final CameraPosition getCameraPosition() {
        Parcel zzH = zzH(1, zza());
        CameraPosition cameraPosition = (CameraPosition) com.google.android.gms.internal.maps.zzc.zza(zzH, CameraPosition.CREATOR);
        zzH.recycle();
        return cameraPosition;
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final float getMaxZoomLevel() {
        Parcel zzH = zzH(2, zza());
        float readFloat = zzH.readFloat();
        zzH.recycle();
        return readFloat;
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final float getMinZoomLevel() {
        Parcel zzH = zzH(3, zza());
        float readFloat = zzH.readFloat();
        zzH.recycle();
        return readFloat;
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final IProjectionDelegate getProjection() {
        IProjectionDelegate zzbtVar;
        Parcel zzH = zzH(26, zza());
        IBinder readStrongBinder = zzH.readStrongBinder();
        if (readStrongBinder == null) {
            zzbtVar = null;
        } else {
            IInterface queryLocalInterface = readStrongBinder.queryLocalInterface("com.google.android.gms.maps.internal.IProjectionDelegate");
            zzbtVar = queryLocalInterface instanceof IProjectionDelegate ? (IProjectionDelegate) queryLocalInterface : new zzbt(readStrongBinder);
        }
        zzH.recycle();
        return zzbtVar;
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final IUiSettingsDelegate getUiSettings() {
        IUiSettingsDelegate zzbzVar;
        Parcel zzH = zzH(25, zza());
        IBinder readStrongBinder = zzH.readStrongBinder();
        if (readStrongBinder == null) {
            zzbzVar = null;
        } else {
            IInterface queryLocalInterface = readStrongBinder.queryLocalInterface("com.google.android.gms.maps.internal.IUiSettingsDelegate");
            zzbzVar = queryLocalInterface instanceof IUiSettingsDelegate ? (IUiSettingsDelegate) queryLocalInterface : new zzbz(readStrongBinder);
        }
        zzH.recycle();
        return zzbzVar;
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void moveCamera(IObjectWrapper iObjectWrapper) {
        Parcel zza = zza();
        com.google.android.gms.internal.maps.zzc.zzg(zza, iObjectWrapper);
        zzc(4, zza);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final boolean setMapStyle(MapStyleOptions mapStyleOptions) {
        Parcel zza = zza();
        com.google.android.gms.internal.maps.zzc.zze(zza, mapStyleOptions);
        Parcel zzH = zzH(91, zza);
        boolean zzh = com.google.android.gms.internal.maps.zzc.zzh(zzH);
        zzH.recycle();
        return zzh;
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void setMapType(int i) {
        Parcel zza = zza();
        zza.writeInt(i);
        zzc(16, zza);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void setMyLocationEnabled(boolean z) {
        Parcel zza = zza();
        com.google.android.gms.internal.maps.zzc.zzd(zza, z);
        zzc(22, zza);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void setOnCameraIdleListener(zzp zzpVar) {
        Parcel zza = zza();
        com.google.android.gms.internal.maps.zzc.zzg(zza, zzpVar);
        zzc(99, zza);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void setOnCameraMoveListener(zzt zztVar) {
        Parcel zza = zza();
        com.google.android.gms.internal.maps.zzc.zzg(zza, zztVar);
        zzc(97, zza);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void setOnCameraMoveStartedListener(zzv zzvVar) {
        Parcel zza = zza();
        com.google.android.gms.internal.maps.zzc.zzg(zza, zzvVar);
        zzc(96, zza);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void setOnMapLoadedCallback(zzao zzaoVar) {
        Parcel zza = zza();
        com.google.android.gms.internal.maps.zzc.zzg(zza, zzaoVar);
        zzc(42, zza);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void setOnMarkerClickListener(zzau zzauVar) {
        Parcel zza = zza();
        com.google.android.gms.internal.maps.zzc.zzg(zza, zzauVar);
        zzc(30, zza);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void setOnMyLocationChangeListener(zzba zzbaVar) {
        Parcel zza = zza();
        com.google.android.gms.internal.maps.zzc.zzg(zza, zzbaVar);
        zzc(36, zza);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void setPadding(int i, int i2, int i3, int i4) {
        Parcel zza = zza();
        zza.writeInt(i);
        zza.writeInt(i2);
        zza.writeInt(i3);
        zza.writeInt(i4);
        zzc(39, zza);
    }
}
