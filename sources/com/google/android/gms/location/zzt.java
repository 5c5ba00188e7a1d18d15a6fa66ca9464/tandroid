package com.google.android.gms.location;

import android.os.IBinder;
import android.os.IInterface;
import com.google.android.gms.internal.location.zzb;

/* loaded from: classes.dex */
public abstract class zzt extends zzb implements zzu {
    public static zzu zzb(IBinder iBinder) {
        IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.location.ILocationListener");
        return queryLocalInterface instanceof zzu ? (zzu) queryLocalInterface : new zzs(iBinder);
    }
}
