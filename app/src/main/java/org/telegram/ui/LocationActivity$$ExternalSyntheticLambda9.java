package org.telegram.ui;

import com.google.android.gms.maps.GoogleMap;
/* loaded from: classes3.dex */
public final /* synthetic */ class LocationActivity$$ExternalSyntheticLambda9 implements GoogleMap.OnCameraMoveListener {
    public final /* synthetic */ LocationActivity f$0;

    public /* synthetic */ LocationActivity$$ExternalSyntheticLambda9(LocationActivity locationActivity) {
        this.f$0 = locationActivity;
    }

    @Override // com.google.android.gms.maps.GoogleMap.OnCameraMoveListener
    public final void onCameraMove() {
        this.f$0.lambda$onMapInit$30();
    }
}
