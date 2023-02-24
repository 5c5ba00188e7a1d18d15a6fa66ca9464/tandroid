package com.google.android.gms.internal.maps;

import android.os.IInterface;
import android.os.RemoteException;
import com.google.android.gms.maps.model.LatLng;
/* compiled from: com.google.android.gms:play-services-maps@@18.1.0 */
/* loaded from: classes.dex */
public interface zzl extends IInterface {
    double zzd() throws RemoteException;

    int zzi() throws RemoteException;

    void zzn() throws RemoteException;

    void zzo(LatLng latLng) throws RemoteException;

    void zzq(int i) throws RemoteException;

    void zzr(double d) throws RemoteException;

    void zzs(int i) throws RemoteException;

    boolean zzy(zzl zzlVar) throws RemoteException;
}
