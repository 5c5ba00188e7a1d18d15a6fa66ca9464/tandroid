package org.telegram.ui;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
/* loaded from: classes3.dex */
public final /* synthetic */ class LocationActivity$$ExternalSyntheticLambda11 implements GoogleMap.OnMarkerClickListener {
    public final /* synthetic */ LocationActivity f$0;

    public /* synthetic */ LocationActivity$$ExternalSyntheticLambda11(LocationActivity locationActivity) {
        this.f$0 = locationActivity;
    }

    @Override // com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
    public final boolean onMarkerClick(Marker marker) {
        boolean lambda$onMapInit$29;
        lambda$onMapInit$29 = this.f$0.lambda$onMapInit$29(marker);
        return lambda$onMapInit$29;
    }
}
