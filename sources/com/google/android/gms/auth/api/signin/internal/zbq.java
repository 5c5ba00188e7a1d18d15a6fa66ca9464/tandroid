package com.google.android.gms.auth.api.signin.internal;

import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.Status;
import org.telegram.messenger.FileLoader;
/* compiled from: com.google.android.gms:play-services-auth@@20.4.0 */
/* loaded from: classes.dex */
public abstract class zbq extends com.google.android.gms.internal.auth-api.zbb implements zbr {
    public zbq() {
        super("com.google.android.gms.auth.api.signin.internal.ISignInCallbacks");
    }

    @Override // com.google.android.gms.internal.auth-api.zbb
    protected final boolean zba(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        switch (i) {
            case FileLoader.MEDIA_DIR_VIDEO_PUBLIC /* 101 */:
                com.google.android.gms.internal.auth-api.zbc.zbb(parcel);
                zbd((GoogleSignInAccount) com.google.android.gms.internal.auth-api.zbc.zba(parcel, GoogleSignInAccount.CREATOR), (Status) com.google.android.gms.internal.auth-api.zbc.zba(parcel, Status.CREATOR));
                break;
            case 102:
                com.google.android.gms.internal.auth-api.zbc.zbb(parcel);
                zbc((Status) com.google.android.gms.internal.auth-api.zbc.zba(parcel, Status.CREATOR));
                break;
            case 103:
                com.google.android.gms.internal.auth-api.zbc.zbb(parcel);
                zbb((Status) com.google.android.gms.internal.auth-api.zbc.zba(parcel, Status.CREATOR));
                break;
            default:
                return false;
        }
        parcel2.writeNoException();
        return true;
    }
}
