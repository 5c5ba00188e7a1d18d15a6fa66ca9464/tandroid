package com.google.android.gms.internal.maps;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.maps.model.LatLng;
/* compiled from: com.google.android.gms:play-services-maps@@18.1.0 */
/* loaded from: classes.dex */
public final class zzj extends zza implements zzl {
    /* JADX INFO: Access modifiers changed from: package-private */
    public zzj(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.maps.model.internal.ICircleDelegate");
    }

    @Override // com.google.android.gms.internal.maps.zzl
    public final double zzd() throws RemoteException {
        Parcel zzH = zzH(6, zza());
        double readDouble = zzH.readDouble();
        zzH.recycle();
        return readDouble;
    }

    @Override // com.google.android.gms.internal.maps.zzl
    public final int zzi() throws RemoteException {
        Parcel zzH = zzH(18, zza());
        int readInt = zzH.readInt();
        zzH.recycle();
        return readInt;
    }

    @Override // com.google.android.gms.internal.maps.zzl
    public final void zzn() throws RemoteException {
        zzc(1, zza());
    }

    @Override // com.google.android.gms.internal.maps.zzl
    public final void zzo(LatLng latLng) throws RemoteException {
        Parcel zza = zza();
        zzc.zze(zza, latLng);
        zzc(3, zza);
    }

    @Override // com.google.android.gms.internal.maps.zzl
    public final void zzq(int i) throws RemoteException {
        Parcel zza = zza();
        zza.writeInt(i);
        zzc(11, zza);
    }

    @Override // com.google.android.gms.internal.maps.zzl
    public final void zzr(double d) throws RemoteException {
        Parcel zza = zza();
        zza.writeDouble(d);
        zzc(5, zza);
    }

    @Override // com.google.android.gms.internal.maps.zzl
    public final void zzs(int i) throws RemoteException {
        Parcel zza = zza();
        zza.writeInt(i);
        zzc(9, zza);
    }

    @Override // com.google.android.gms.internal.maps.zzl
    public final boolean zzy(zzl zzlVar) throws RemoteException {
        Parcel zza = zza();
        zzc.zzg(zza, zzlVar);
        Parcel zzH = zzH(17, zza);
        boolean zzh = zzc.zzh(zzH);
        zzH.recycle();
        return zzh;
    }
}
