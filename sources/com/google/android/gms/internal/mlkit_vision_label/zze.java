package com.google.android.gms.internal.mlkit_vision_label;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
/* compiled from: com.google.android.gms:play-services-mlkit-image-labeling@@16.0.8 */
/* loaded from: classes.dex */
public final class zze extends zza implements zzg {
    /* JADX INFO: Access modifiers changed from: package-private */
    public zze(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.vision.label.internal.client.INativeImageLabelerCreator");
    }

    @Override // com.google.android.gms.internal.mlkit_vision_label.zzg
    public final zzd zzd(IObjectWrapper iObjectWrapper, zzj zzjVar) throws RemoteException {
        zzd zzdVar;
        Parcel zza = zza();
        zzc.zzb(zza, iObjectWrapper);
        zzc.zza(zza, zzjVar);
        Parcel zzb = zzb(1, zza);
        IBinder readStrongBinder = zzb.readStrongBinder();
        if (readStrongBinder == null) {
            zzdVar = null;
        } else {
            IInterface queryLocalInterface = readStrongBinder.queryLocalInterface("com.google.android.gms.vision.label.internal.client.INativeImageLabeler");
            zzdVar = queryLocalInterface instanceof zzd ? (zzd) queryLocalInterface : new zzd(readStrongBinder);
        }
        zzb.recycle();
        return zzdVar;
    }
}
