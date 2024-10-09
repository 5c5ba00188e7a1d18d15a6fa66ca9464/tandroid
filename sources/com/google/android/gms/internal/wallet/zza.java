package com.google.android.gms.internal.wallet;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

/* loaded from: classes.dex */
public abstract class zza implements IInterface {
    private final IBinder zza;
    private final String zzb = "com.google.android.gms.wallet.internal.IOwService";

    /* JADX INFO: Access modifiers changed from: protected */
    public zza(IBinder iBinder, String str) {
        this.zza = iBinder;
    }

    @Override // android.os.IInterface
    public final IBinder asBinder() {
        return this.zza;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final Parcel zza() {
        Parcel obtain = Parcel.obtain();
        obtain.writeInterfaceToken(this.zzb);
        return obtain;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void zzb(int i, Parcel parcel) {
        try {
            this.zza.transact(i, parcel, null, 1);
        } finally {
            parcel.recycle();
        }
    }
}
