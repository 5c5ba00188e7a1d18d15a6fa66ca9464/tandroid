package com.google.android.gms.internal.safetynet;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* compiled from: com.google.android.gms:play-services-safetynet@@17.0.1 */
/* loaded from: classes.dex */
public final class zzh extends zza implements IInterface {
    /* JADX INFO: Access modifiers changed from: package-private */
    public zzh(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.safetynet.internal.ISafetyNetService");
    }

    public final void zzc(zzg zzgVar, byte[] bArr, String str) throws RemoteException {
        Parcel zza = zza();
        zzc.zzb(zza, zzgVar);
        zza.writeByteArray(bArr);
        zza.writeString(str);
        zzb(7, zza);
    }
}
