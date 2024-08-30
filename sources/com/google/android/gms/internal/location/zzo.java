package com.google.android.gms.internal.location;

import android.location.Location;
import android.os.IInterface;
import com.google.android.gms.common.api.internal.IStatusCallback;
import com.google.android.gms.location.LastLocationRequest;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
/* loaded from: classes.dex */
public interface zzo extends IInterface {
    Location zzd();

    void zzh(LocationSettingsRequest locationSettingsRequest, zzs zzsVar, String str);

    void zzj(LastLocationRequest lastLocationRequest, zzq zzqVar);

    void zzk(zzdb zzdbVar, LocationRequest locationRequest, IStatusCallback iStatusCallback);

    void zzy(zzdb zzdbVar, IStatusCallback iStatusCallback);

    void zzz(zzdf zzdfVar);
}
