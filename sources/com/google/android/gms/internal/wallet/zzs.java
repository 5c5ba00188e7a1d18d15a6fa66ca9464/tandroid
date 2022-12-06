package com.google.android.gms.internal.wallet;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentDataRequest;
/* compiled from: com.google.android.gms:play-services-wallet@@19.1.0 */
/* loaded from: classes.dex */
public final class zzs extends zza {
    /* JADX INFO: Access modifiers changed from: package-private */
    public zzs(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.wallet.internal.IOwService");
    }

    public final void zze(IsReadyToPayRequest isReadyToPayRequest, Bundle bundle, zzu zzuVar) throws RemoteException {
        Parcel zza = zza();
        zzc.zzb(zza, isReadyToPayRequest);
        zzc.zzb(zza, bundle);
        zzc.zzc(zza, zzuVar);
        zzb(14, zza);
    }

    public final void zzf(PaymentDataRequest paymentDataRequest, Bundle bundle, zzu zzuVar) throws RemoteException {
        Parcel zza = zza();
        zzc.zzb(zza, paymentDataRequest);
        zzc.zzb(zza, bundle);
        zzc.zzc(zza, zzuVar);
        zzb(19, zza);
    }
}
