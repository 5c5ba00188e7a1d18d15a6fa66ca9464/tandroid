package com.google.android.gms.internal.mlkit_vision_label;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
/* compiled from: com.google.android.gms:play-services-mlkit-image-labeling@@16.0.8 */
/* loaded from: classes.dex */
public final class zzd extends zza implements IInterface {
    /* JADX INFO: Access modifiers changed from: package-private */
    public zzd(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.vision.label.internal.client.INativeImageLabeler");
    }

    public final void zzd() throws RemoteException {
        zzc(2, zza());
    }

    public final zzh[] zze(IObjectWrapper iObjectWrapper, zzl zzlVar) throws RemoteException {
        Parcel zza = zza();
        zzc.zzb(zza, iObjectWrapper);
        zzc.zza(zza, zzlVar);
        Parcel zzb = zzb(1, zza);
        zzh[] zzhVarArr = (zzh[]) zzb.createTypedArray(zzh.CREATOR);
        zzb.recycle();
        return zzhVarArr;
    }
}
