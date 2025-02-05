package com.google.android.gms.internal.base;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

/* loaded from: classes.dex */
public abstract class zaa implements IInterface {
    private final IBinder zaa;
    private final String zab;

    protected zaa(IBinder iBinder, String str) {
        this.zaa = iBinder;
        this.zab = str;
    }

    @Override // android.os.IInterface
    public final IBinder asBinder() {
        return this.zaa;
    }

    protected final Parcel zaa() {
        Parcel obtain = Parcel.obtain();
        obtain.writeInterfaceToken(this.zab);
        return obtain;
    }

    protected final void zac(int i, Parcel parcel) {
        Parcel obtain = Parcel.obtain();
        try {
            this.zaa.transact(i, parcel, obtain, 0);
            obtain.readException();
        } finally {
            parcel.recycle();
            obtain.recycle();
        }
    }

    protected final void zad(int i, Parcel parcel) {
        try {
            this.zaa.transact(1, parcel, null, 1);
        } finally {
            parcel.recycle();
        }
    }
}
