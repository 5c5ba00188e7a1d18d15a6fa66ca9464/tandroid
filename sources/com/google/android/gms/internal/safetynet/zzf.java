package com.google.android.gms.internal.safetynet;

import android.os.Parcel;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.safetynet.SafeBrowsingData;
/* loaded from: classes.dex */
public abstract class zzf extends zzb implements zzg {
    public zzf() {
        super("com.google.android.gms.safetynet.internal.ISafetyNetCallbacks");
    }

    @Override // com.google.android.gms.internal.safetynet.zzb
    protected final boolean zza(int i, Parcel parcel, Parcel parcel2, int i2) {
        if (i == 1) {
            zzd((Status) zzc.zza(parcel, Status.CREATOR), (com.google.android.gms.safetynet.zza) zzc.zza(parcel, com.google.android.gms.safetynet.zza.CREATOR));
        } else if (i == 2) {
            zze(parcel.readString());
        } else if (i == 3) {
            zzj((Status) zzc.zza(parcel, Status.CREATOR), (SafeBrowsingData) zzc.zza(parcel, SafeBrowsingData.CREATOR));
        } else if (i == 4) {
            zzb((Status) zzc.zza(parcel, Status.CREATOR), zzc.zzc(parcel));
        } else if (i == 6) {
            zzh((Status) zzc.zza(parcel, Status.CREATOR), (com.google.android.gms.safetynet.zzf) zzc.zza(parcel, com.google.android.gms.safetynet.zzf.CREATOR));
        } else if (i == 8) {
            zzg((Status) zzc.zza(parcel, Status.CREATOR), (com.google.android.gms.safetynet.zzd) zzc.zza(parcel, com.google.android.gms.safetynet.zzd.CREATOR));
        } else if (i == 10) {
            zzf((Status) zzc.zza(parcel, Status.CREATOR), zzc.zzc(parcel));
        } else if (i == 11) {
            zzc((Status) zzc.zza(parcel, Status.CREATOR));
        } else if (i == 15) {
            zzi((Status) zzc.zza(parcel, Status.CREATOR), (com.google.android.gms.safetynet.zzh) zzc.zza(parcel, com.google.android.gms.safetynet.zzh.CREATOR));
        } else if (i != 16) {
            return false;
        } else {
            zzk((Status) zzc.zza(parcel, Status.CREATOR), parcel.readString(), parcel.readInt());
        }
        return true;
    }
}
