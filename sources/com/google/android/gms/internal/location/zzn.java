package com.google.android.gms.internal.location;

import android.location.Location;
import android.os.IBinder;
import android.os.Parcel;
import com.google.android.gms.common.api.internal.IStatusCallback;
import com.google.android.gms.location.LastLocationRequest;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;

/* loaded from: classes.dex */
public final class zzn extends zza implements zzo {
    zzn(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.location.internal.IGoogleLocationManagerService");
    }

    @Override // com.google.android.gms.internal.location.zzo
    public final Location zzd() {
        Parcel zzb = zzb(7, zza());
        Location location = (Location) zzc.zza(zzb, Location.CREATOR);
        zzb.recycle();
        return location;
    }

    @Override // com.google.android.gms.internal.location.zzo
    public final void zzh(LocationSettingsRequest locationSettingsRequest, zzs zzsVar, String str) {
        Parcel zza = zza();
        zzc.zzd(zza, locationSettingsRequest);
        zzc.zze(zza, zzsVar);
        zza.writeString(null);
        zzc(63, zza);
    }

    @Override // com.google.android.gms.internal.location.zzo
    public final void zzj(LastLocationRequest lastLocationRequest, zzq zzqVar) {
        Parcel zza = zza();
        zzc.zzd(zza, lastLocationRequest);
        zzc.zze(zza, zzqVar);
        zzc(82, zza);
    }

    @Override // com.google.android.gms.internal.location.zzo
    public final void zzk(zzdb zzdbVar, LocationRequest locationRequest, IStatusCallback iStatusCallback) {
        Parcel zza = zza();
        zzc.zzd(zza, zzdbVar);
        zzc.zzd(zza, locationRequest);
        zzc.zze(zza, iStatusCallback);
        zzc(88, zza);
    }

    @Override // com.google.android.gms.internal.location.zzo
    public final void zzy(zzdb zzdbVar, IStatusCallback iStatusCallback) {
        Parcel zza = zza();
        zzc.zzd(zza, zzdbVar);
        zzc.zze(zza, iStatusCallback);
        zzc(89, zza);
    }

    @Override // com.google.android.gms.internal.location.zzo
    public final void zzz(zzdf zzdfVar) {
        Parcel zza = zza();
        zzc.zzd(zza, zzdfVar);
        zzc(59, zza);
    }
}
