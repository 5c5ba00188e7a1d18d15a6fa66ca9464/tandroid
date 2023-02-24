package com.google.android.gms.internal.maps;

import android.os.IInterface;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.maps.model.LatLng;
/* compiled from: com.google.android.gms:play-services-maps@@18.1.0 */
/* loaded from: classes.dex */
public interface zzaa extends IInterface {
    boolean zzC(zzaa zzaaVar) throws RemoteException;

    int zzg() throws RemoteException;

    IObjectWrapper zzh() throws RemoteException;

    LatLng zzi() throws RemoteException;

    void zzn() throws RemoteException;

    void zzs(IObjectWrapper iObjectWrapper) throws RemoteException;

    void zzu(LatLng latLng) throws RemoteException;

    void zzv(float f) throws RemoteException;

    void zzx(IObjectWrapper iObjectWrapper) throws RemoteException;
}
