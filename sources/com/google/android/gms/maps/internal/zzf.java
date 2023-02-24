package com.google.android.gms.maps.internal;

import android.os.IInterface;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.internal.maps.zzi;
import com.google.android.gms.maps.GoogleMapOptions;
/* compiled from: com.google.android.gms:play-services-maps@@18.1.0 */
/* loaded from: classes.dex */
public interface zzf extends IInterface {
    int zzd() throws RemoteException;

    ICameraUpdateFactoryDelegate zze() throws RemoteException;

    IMapViewDelegate zzg(IObjectWrapper iObjectWrapper, GoogleMapOptions googleMapOptions) throws RemoteException;

    zzi zzj() throws RemoteException;

    void zzk(IObjectWrapper iObjectWrapper, int i) throws RemoteException;

    void zzl(IObjectWrapper iObjectWrapper, int i) throws RemoteException;
}
