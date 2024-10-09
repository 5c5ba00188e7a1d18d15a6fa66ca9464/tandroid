package com.google.android.gms.internal.play_billing;

import android.os.IBinder;
import android.os.IInterface;

/* loaded from: classes.dex */
public abstract class zzd extends zzi implements zze {
    public static zze zzn(IBinder iBinder) {
        if (iBinder == null) {
            return null;
        }
        IInterface queryLocalInterface = iBinder.queryLocalInterface("com.android.vending.billing.IInAppBillingService");
        return queryLocalInterface instanceof zze ? (zze) queryLocalInterface : new zzc(iBinder);
    }
}
