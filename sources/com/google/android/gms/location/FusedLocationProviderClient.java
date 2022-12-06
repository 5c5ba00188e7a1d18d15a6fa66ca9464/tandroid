package com.google.android.gms.location;

import android.location.Location;
import android.os.Looper;
import com.google.android.gms.tasks.Task;
/* compiled from: com.google.android.gms:play-services-location@@21.0.1 */
/* loaded from: classes.dex */
public interface FusedLocationProviderClient {
    Task<Location> getLastLocation();

    Task<Void> removeLocationUpdates(LocationCallback locationCallback);

    Task<Void> requestLocationUpdates(LocationRequest locationRequest, LocationCallback locationCallback, Looper looper);
}
