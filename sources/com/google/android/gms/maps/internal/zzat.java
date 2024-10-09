package com.google.android.gms.maps.internal;

import android.os.Parcel;
import com.google.android.gms.internal.maps.zzaa;
import com.google.android.gms.internal.maps.zzz;

/* loaded from: classes.dex */
public abstract class zzat extends com.google.android.gms.internal.maps.zzb implements zzau {
    public zzat() {
        super("com.google.android.gms.maps.internal.IOnMarkerClickListener");
    }

    @Override // com.google.android.gms.internal.maps.zzb
    protected final boolean zza(int i, Parcel parcel, Parcel parcel2, int i2) {
        if (i != 1) {
            return false;
        }
        zzaa zzb = zzz.zzb(parcel.readStrongBinder());
        com.google.android.gms.internal.maps.zzc.zzc(parcel);
        boolean zzb2 = zzb(zzb);
        parcel2.writeNoException();
        com.google.android.gms.internal.maps.zzc.zzd(parcel2, zzb2);
        return true;
    }
}
