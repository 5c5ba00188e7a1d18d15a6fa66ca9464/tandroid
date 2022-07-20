package org.telegram.ui;

import android.location.Location;
import com.google.android.gms.maps.GoogleMap;
/* loaded from: classes3.dex */
public final /* synthetic */ class LocationActivity$$ExternalSyntheticLambda12 implements GoogleMap.OnMyLocationChangeListener {
    public final /* synthetic */ LocationActivity f$0;

    public /* synthetic */ LocationActivity$$ExternalSyntheticLambda12(LocationActivity locationActivity) {
        this.f$0 = locationActivity;
    }

    @Override // com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener
    public final void onMyLocationChange(Location location) {
        this.f$0.lambda$onMapInit$28(location);
    }
}
