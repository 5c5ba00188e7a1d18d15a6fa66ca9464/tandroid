package com.google.android.gms.auth.api.signin.internal;

import android.os.Parcel;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.Status;

/* loaded from: classes.dex */
public abstract class zbq extends com.google.android.gms.internal.auth-api.zbb implements zbr {
    public zbq() {
        super("com.google.android.gms.auth.api.signin.internal.ISignInCallbacks");
    }

    @Override // com.google.android.gms.internal.auth-api.zbb
    protected final boolean zba(int i, Parcel parcel, Parcel parcel2, int i2) {
        switch (i) {
            case 101:
                GoogleSignInAccount googleSignInAccount = (GoogleSignInAccount) com.google.android.gms.internal.auth-api.zbc.zba(parcel, GoogleSignInAccount.CREATOR);
                Status status = (Status) com.google.android.gms.internal.auth-api.zbc.zba(parcel, Status.CREATOR);
                com.google.android.gms.internal.auth-api.zbc.zbb(parcel);
                zbd(googleSignInAccount, status);
                break;
            case 102:
                Status status2 = (Status) com.google.android.gms.internal.auth-api.zbc.zba(parcel, Status.CREATOR);
                com.google.android.gms.internal.auth-api.zbc.zbb(parcel);
                zbc(status2);
                break;
            case 103:
                Status status3 = (Status) com.google.android.gms.internal.auth-api.zbc.zba(parcel, Status.CREATOR);
                com.google.android.gms.internal.auth-api.zbc.zbb(parcel);
                zbb(status3);
                break;
            default:
                return false;
        }
        parcel2.writeNoException();
        return true;
    }
}
