package com.google.android.gms.internal.location;

import android.location.Location;
import android.os.IInterface;
import android.os.RemoteException;
import com.google.android.gms.common.api.internal.IStatusCallback;
import com.google.android.gms.location.LastLocationRequest;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
/* compiled from: com.google.android.gms:play-services-location@@21.0.1 */
/* loaded from: classes.dex */
public interface zzo extends IInterface {
    @Deprecated
    Location zzd() throws RemoteException;

    void zzh(LocationSettingsRequest locationSettingsRequest, zzs zzsVar, String str) throws RemoteException;

    @Deprecated
    void zzj(LastLocationRequest lastLocationRequest, zzq zzqVar) throws RemoteException;

    void zzk(zzdb zzdbVar, LocationRequest locationRequest, IStatusCallback iStatusCallback) throws RemoteException;

    void zzy(zzdb zzdbVar, IStatusCallback iStatusCallback) throws RemoteException;

    @Deprecated
    void zzz(zzdf zzdfVar) throws RemoteException;
}
