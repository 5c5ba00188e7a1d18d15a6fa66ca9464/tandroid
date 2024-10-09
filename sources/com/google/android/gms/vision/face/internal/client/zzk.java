package com.google.android.gms.vision.face.internal.client;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import com.google.android.gms.dynamic.IObjectWrapper;

/* loaded from: classes.dex */
public final class zzk extends com.google.android.gms.internal.vision.zzb implements zzi {
    /* JADX INFO: Access modifiers changed from: package-private */
    public zzk(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.vision.face.internal.client.INativeFaceDetectorCreator");
    }

    @Override // com.google.android.gms.vision.face.internal.client.zzi
    public final zzh newFaceDetector(IObjectWrapper iObjectWrapper, zzf zzfVar) {
        zzh zzjVar;
        Parcel a_ = a_();
        com.google.android.gms.internal.vision.zzd.zza(a_, iObjectWrapper);
        com.google.android.gms.internal.vision.zzd.zza(a_, zzfVar);
        Parcel zza = zza(1, a_);
        IBinder readStrongBinder = zza.readStrongBinder();
        if (readStrongBinder == null) {
            zzjVar = null;
        } else {
            IInterface queryLocalInterface = readStrongBinder.queryLocalInterface("com.google.android.gms.vision.face.internal.client.INativeFaceDetector");
            zzjVar = queryLocalInterface instanceof zzh ? (zzh) queryLocalInterface : new zzj(readStrongBinder);
        }
        zza.recycle();
        return zzjVar;
    }
}
