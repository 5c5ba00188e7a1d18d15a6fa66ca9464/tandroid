package com.google.android.gms.location;

import com.google.android.gms.tasks.Task;
/* compiled from: com.google.android.gms:play-services-location@@21.0.1 */
/* loaded from: classes.dex */
public interface SettingsClient {
    Task<LocationSettingsResponse> checkLocationSettings(LocationSettingsRequest locationSettingsRequest);
}
