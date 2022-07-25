package com.google.android.aidl;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes.dex */
public abstract class BaseProxy implements IInterface {
    private final String mDescriptor;
    private final IBinder mRemote;

    /* JADX INFO: Access modifiers changed from: protected */
    public BaseProxy(IBinder remote, String descriptor) {
        this.mRemote = remote;
        this.mDescriptor = descriptor;
    }

    @Override // android.os.IInterface
    public IBinder asBinder() {
        return this.mRemote;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Parcel obtainAndWriteInterfaceToken() {
        Parcel obtain = Parcel.obtain();
        obtain.writeInterfaceToken(this.mDescriptor);
        return obtain;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Parcel transactAndReadException(int code, Parcel in) throws RemoteException {
        in = Parcel.obtain();
        try {
            this.mRemote.transact(code, in, in, 0);
            in.readException();
            return in;
        } catch (RuntimeException e) {
            throw e;
        } finally {
            in.recycle();
        }
    }
}
