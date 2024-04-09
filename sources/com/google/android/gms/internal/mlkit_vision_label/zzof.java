package com.google.android.gms.internal.mlkit_vision_label;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
import java.util.ArrayList;
import java.util.List;
/* compiled from: com.google.android.gms:play-services-mlkit-image-labeling@@16.0.8 */
/* loaded from: classes.dex */
public final class zzof extends zza {
    /* JADX INFO: Access modifiers changed from: package-private */
    public zzof(IBinder iBinder) {
        super(iBinder, "com.google.mlkit.vision.label.aidls.IImageLabeler");
    }

    public final List zzd(IObjectWrapper iObjectWrapper, zzod zzodVar) throws RemoteException {
        Parcel zza = zza();
        zzc.zzb(zza, iObjectWrapper);
        zzc.zza(zza, zzodVar);
        Parcel zzb = zzb(3, zza);
        ArrayList createTypedArrayList = zzb.createTypedArrayList(zzoj.CREATOR);
        zzb.recycle();
        return createTypedArrayList;
    }

    public final void zze() throws RemoteException {
        zzc(1, zza());
    }

    public final void zzf() throws RemoteException {
        zzc(2, zza());
    }
}
