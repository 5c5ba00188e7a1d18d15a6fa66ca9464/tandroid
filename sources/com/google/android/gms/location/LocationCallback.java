package com.google.android.gms.location;

/* loaded from: classes.dex */
public abstract class LocationCallback {
    public void onLocationAvailability(LocationAvailability locationAvailability) {
    }

    public abstract void onLocationResult(LocationResult locationResult);
}
