package com.google.android.gms.location;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import com.google.android.gms.internal.location.zzb;
import com.google.android.gms.internal.location.zzc;
/* loaded from: classes.dex */
public abstract class zzq extends zzb implements zzr {
    public zzq() {
        super("com.google.android.gms.location.ILocationCallback");
    }

    public static zzr zzb(IBinder iBinder) {
        IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.location.ILocationCallback");
        return queryLocalInterface instanceof zzr ? (zzr) queryLocalInterface : new zzp(iBinder);
    }

    @Override // com.google.android.gms.internal.location.zzb
    protected final boolean zza(int i, Parcel parcel, Parcel parcel2, int i2) {
        if (i == 1) {
            zzc.zzb(parcel);
            zze((LocationResult) zzc.zza(parcel, LocationResult.CREATOR));
        } else if (i == 2) {
            zzc.zzb(parcel);
            zzd((LocationAvailability) zzc.zza(parcel, LocationAvailability.CREATOR));
        } else if (i != 3) {
            return false;
        } else {
            zzf();
        }
        return true;
    }
}
