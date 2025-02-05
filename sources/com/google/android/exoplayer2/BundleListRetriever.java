package com.google.android.exoplayer2;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.List;

/* loaded from: classes.dex */
public final class BundleListRetriever extends Binder {
    private static final int SUGGESTED_MAX_IPC_SIZE;
    private final ImmutableList list;

    static {
        SUGGESTED_MAX_IPC_SIZE = Util.SDK_INT >= 30 ? IBinder.getSuggestedMaxIpcSizeBytes() : 65536;
    }

    public BundleListRetriever(List list) {
        this.list = ImmutableList.copyOf((Collection) list);
    }

    public static ImmutableList getList(IBinder iBinder) {
        int readInt;
        ImmutableList.Builder builder = ImmutableList.builder();
        int i = 1;
        int i2 = 0;
        while (i != 0) {
            Parcel obtain = Parcel.obtain();
            Parcel obtain2 = Parcel.obtain();
            try {
                obtain.writeInt(i2);
                try {
                    iBinder.transact(1, obtain, obtain2, 0);
                    while (true) {
                        readInt = obtain2.readInt();
                        if (readInt == 1) {
                            builder.add(Assertions.checkNotNull(obtain2.readBundle()));
                            i2++;
                        }
                    }
                    obtain2.recycle();
                    obtain.recycle();
                    i = readInt;
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            } catch (Throwable th) {
                obtain2.recycle();
                obtain.recycle();
                throw th;
            }
        }
        return builder.build();
    }

    @Override // android.os.Binder
    protected boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
        if (i != 1) {
            return super.onTransact(i, parcel, parcel2, i2);
        }
        if (parcel2 == null) {
            return false;
        }
        int size = this.list.size();
        int readInt = parcel.readInt();
        while (readInt < size && parcel2.dataSize() < SUGGESTED_MAX_IPC_SIZE) {
            parcel2.writeInt(1);
            parcel2.writeBundle((Bundle) this.list.get(readInt));
            readInt++;
        }
        parcel2.writeInt(readInt < size ? 2 : 0);
        return true;
    }
}
