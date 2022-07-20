package org.telegram.ui;

import com.google.android.gms.maps.GoogleMap;
/* loaded from: classes3.dex */
public final /* synthetic */ class LocationActivity$$ExternalSyntheticLambda10 implements GoogleMap.OnCameraMoveStartedListener {
    public final /* synthetic */ LocationActivity f$0;

    public /* synthetic */ LocationActivity$$ExternalSyntheticLambda10(LocationActivity locationActivity) {
        this.f$0 = locationActivity;
    }

    @Override // com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener
    public final void onCameraMoveStarted(int i) {
        this.f$0.lambda$onMapInit$27(i);
    }
}
