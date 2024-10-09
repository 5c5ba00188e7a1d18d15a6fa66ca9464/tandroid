package com.google.android.gms.signin.internal;

import android.os.Parcel;

/* loaded from: classes.dex */
public abstract class zad extends com.google.android.gms.internal.base.zab implements zae {
    public zad() {
        super("com.google.android.gms.signin.internal.ISignInCallbacks");
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:1:0x0000. Please report as an issue. */
    @Override // com.google.android.gms.internal.base.zab
    protected final boolean zaa(int i, Parcel parcel, Parcel parcel2, int i2) {
        switch (i) {
            case 3:
                com.google.android.gms.internal.base.zac.zab(parcel);
                parcel2.writeNoException();
                return true;
            case 4:
            case 6:
                com.google.android.gms.internal.base.zac.zab(parcel);
                parcel2.writeNoException();
                return true;
            case 5:
            default:
                return false;
            case 7:
                com.google.android.gms.internal.base.zac.zab(parcel);
                parcel2.writeNoException();
                return true;
            case 8:
                zak zakVar = (zak) com.google.android.gms.internal.base.zac.zaa(parcel, zak.CREATOR);
                com.google.android.gms.internal.base.zac.zab(parcel);
                zab(zakVar);
                parcel2.writeNoException();
                return true;
            case 9:
                com.google.android.gms.internal.base.zac.zab(parcel);
                parcel2.writeNoException();
                return true;
        }
    }
}
