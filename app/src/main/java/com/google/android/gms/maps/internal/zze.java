package com.google.android.gms.maps.internal;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.internal.maps.zzh;
import com.google.android.gms.internal.maps.zzi;
import com.google.android.gms.maps.GoogleMapOptions;
/* compiled from: com.google.android.gms:play-services-maps@@17.0.1 */
/* loaded from: classes.dex */
public final class zze extends com.google.android.gms.internal.maps.zza implements zzf {
    /* JADX INFO: Access modifiers changed from: package-private */
    public zze(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.maps.internal.ICreator");
    }

    @Override // com.google.android.gms.maps.internal.zzf
    public final IMapViewDelegate zze(IObjectWrapper iObjectWrapper, GoogleMapOptions googleMapOptions) throws RemoteException {
        IMapViewDelegate zzlVar;
        Parcel zza = zza();
        com.google.android.gms.internal.maps.zzc.zzf(zza, iObjectWrapper);
        com.google.android.gms.internal.maps.zzc.zzd(zza, googleMapOptions);
        Parcel zzH = zzH(3, zza);
        IBinder readStrongBinder = zzH.readStrongBinder();
        if (readStrongBinder == null) {
            zzlVar = null;
        } else {
            IInterface queryLocalInterface = readStrongBinder.queryLocalInterface("com.google.android.gms.maps.internal.IMapViewDelegate");
            if (queryLocalInterface instanceof IMapViewDelegate) {
                zzlVar = (IMapViewDelegate) queryLocalInterface;
            } else {
                zzlVar = new zzl(readStrongBinder);
            }
        }
        zzH.recycle();
        return zzlVar;
    }

    @Override // com.google.android.gms.maps.internal.zzf
    public final ICameraUpdateFactoryDelegate zzf() throws RemoteException {
        ICameraUpdateFactoryDelegate zzbVar;
        Parcel zzH = zzH(4, zza());
        IBinder readStrongBinder = zzH.readStrongBinder();
        if (readStrongBinder == null) {
            zzbVar = null;
        } else {
            IInterface queryLocalInterface = readStrongBinder.queryLocalInterface("com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate");
            if (queryLocalInterface instanceof ICameraUpdateFactoryDelegate) {
                zzbVar = (ICameraUpdateFactoryDelegate) queryLocalInterface;
            } else {
                zzbVar = new zzb(readStrongBinder);
            }
        }
        zzH.recycle();
        return zzbVar;
    }

    @Override // com.google.android.gms.maps.internal.zzf
    public final zzi zzg() throws RemoteException {
        Parcel zzH = zzH(5, zza());
        zzi zzb = zzh.zzb(zzH.readStrongBinder());
        zzH.recycle();
        return zzb;
    }

    @Override // com.google.android.gms.maps.internal.zzf
    public final void zzh(IObjectWrapper iObjectWrapper, int i) throws RemoteException {
        Parcel zza = zza();
        com.google.android.gms.internal.maps.zzc.zzf(zza, iObjectWrapper);
        zza.writeInt(i);
        zzc(6, zza);
    }
}
