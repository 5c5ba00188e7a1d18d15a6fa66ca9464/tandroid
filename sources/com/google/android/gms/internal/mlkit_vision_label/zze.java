package com.google.android.gms.internal.mlkit_vision_label;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import com.google.android.gms.dynamic.IObjectWrapper;

/* loaded from: classes.dex */
public final class zze extends zza implements zzg {
    zze(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.vision.label.internal.client.INativeImageLabelerCreator");
    }

    @Override // com.google.android.gms.internal.mlkit_vision_label.zzg
    public final zzd zzd(IObjectWrapper iObjectWrapper, zzj zzjVar) {
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
