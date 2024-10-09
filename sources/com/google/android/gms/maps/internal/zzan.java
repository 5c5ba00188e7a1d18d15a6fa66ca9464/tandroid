package com.google.android.gms.maps.internal;

import android.os.Parcel;

/* loaded from: classes.dex */
public abstract class zzan extends com.google.android.gms.internal.maps.zzb implements zzao {
    public zzan() {
        super("com.google.android.gms.maps.internal.IOnMapLoadedCallback");
    }

    @Override // com.google.android.gms.internal.maps.zzb
    protected final boolean zza(int i, Parcel parcel, Parcel parcel2, int i2) {
        if (i != 1) {
            return false;
        }
        zzb();
        parcel2.writeNoException();
        return true;
    }
}
