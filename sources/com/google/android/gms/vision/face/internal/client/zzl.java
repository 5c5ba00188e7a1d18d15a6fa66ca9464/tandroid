package com.google.android.gms.vision.face.internal.client;

import android.os.IBinder;
import android.os.IInterface;

/* loaded from: classes.dex */
public abstract class zzl extends com.google.android.gms.internal.vision.zza implements zzi {
    public static zzi asInterface(IBinder iBinder) {
        if (iBinder == null) {
            return null;
        }
        IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.vision.face.internal.client.INativeFaceDetectorCreator");
        return queryLocalInterface instanceof zzi ? (zzi) queryLocalInterface : new zzk(iBinder);
    }
}
