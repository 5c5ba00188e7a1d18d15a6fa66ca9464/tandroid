package com.google.android.gms.internal.auth-api-phone;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

/* loaded from: classes.dex */
public final class zzh extends zza implements IInterface {
    /* JADX INFO: Access modifiers changed from: package-private */
    public zzh(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.auth.api.phone.internal.ISmsRetrieverApiService");
    }

    public final void zzg(zzj zzjVar) {
        Parcel zza = zza();
        zzc.zzb(zza, zzjVar);
        zzb(1, zza);
    }
}
