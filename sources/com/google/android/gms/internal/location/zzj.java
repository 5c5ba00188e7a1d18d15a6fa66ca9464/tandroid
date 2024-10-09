package com.google.android.gms.internal.location;

import android.os.Parcel;

/* loaded from: classes.dex */
public abstract class zzj extends zzb implements zzk {
    public zzj() {
        super("com.google.android.gms.location.internal.IFusedLocationProviderCallback");
    }

    @Override // com.google.android.gms.internal.location.zzb
    protected final boolean zza(int i, Parcel parcel, Parcel parcel2, int i2) {
        if (i == 1) {
            zzg zzgVar = (zzg) zzc.zza(parcel, zzg.CREATOR);
            zzc.zzb(parcel);
            zzd(zzgVar);
        } else {
            if (i != 2) {
                return false;
            }
            zze();
        }
        return true;
    }
}
