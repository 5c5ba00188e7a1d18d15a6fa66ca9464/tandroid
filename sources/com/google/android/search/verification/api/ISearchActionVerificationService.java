package com.google.android.search.verification.api;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import com.google.android.aidl.BaseProxy;
import com.google.android.aidl.BaseStub;
import com.google.android.aidl.Codecs;

/* loaded from: classes.dex */
public interface ISearchActionVerificationService extends IInterface {

    public static abstract class Stub extends BaseStub implements ISearchActionVerificationService {

        public static class Proxy extends BaseProxy implements ISearchActionVerificationService {
            Proxy(IBinder iBinder) {
                super(iBinder, "com.google.android.search.verification.api.ISearchActionVerificationService");
            }

            @Override // com.google.android.search.verification.api.ISearchActionVerificationService
            public int getVersion() {
                Parcel transactAndReadException = transactAndReadException(2, obtainAndWriteInterfaceToken());
                int readInt = transactAndReadException.readInt();
                transactAndReadException.recycle();
                return readInt;
            }

            @Override // com.google.android.search.verification.api.ISearchActionVerificationService
            public boolean isSearchAction(Intent intent, Bundle bundle) {
                Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
                Codecs.writeParcelable(obtainAndWriteInterfaceToken, intent);
                Codecs.writeParcelable(obtainAndWriteInterfaceToken, bundle);
                Parcel transactAndReadException = transactAndReadException(1, obtainAndWriteInterfaceToken);
                boolean createBoolean = Codecs.createBoolean(transactAndReadException);
                transactAndReadException.recycle();
                return createBoolean;
            }
        }

        public static ISearchActionVerificationService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.search.verification.api.ISearchActionVerificationService");
            return queryLocalInterface instanceof ISearchActionVerificationService ? (ISearchActionVerificationService) queryLocalInterface : new Proxy(iBinder);
        }
    }

    int getVersion();

    boolean isSearchAction(Intent intent, Bundle bundle);
}
