package com.google.android.gms.internal.wallet;

import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentDataRequest;

/* loaded from: classes.dex */
public final class zzs extends zza implements IInterface {
    zzs(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.wallet.internal.IOwService");
    }

    public final void zze(IsReadyToPayRequest isReadyToPayRequest, Bundle bundle, zzu zzuVar) {
        Parcel zza = zza();
        zzc.zzb(zza, isReadyToPayRequest);
        zzc.zzb(zza, bundle);
        zzc.zzc(zza, zzuVar);
        zzb(14, zza);
    }

    public final void zzf(PaymentDataRequest paymentDataRequest, Bundle bundle, zzu zzuVar) {
        Parcel zza = zza();
        zzc.zzb(zza, paymentDataRequest);
        zzc.zzb(zza, bundle);
        zzc.zzc(zza, zzuVar);
        zzb(19, zza);
    }
}
