package org.telegram.ui.Components;

import com.google.android.gms.maps.GoogleMap;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatAttachAlertLocationLayout$$ExternalSyntheticLambda5 implements GoogleMap.OnCameraMoveListener {
    public final /* synthetic */ ChatAttachAlertLocationLayout f$0;

    public /* synthetic */ ChatAttachAlertLocationLayout$$ExternalSyntheticLambda5(ChatAttachAlertLocationLayout chatAttachAlertLocationLayout) {
        this.f$0 = chatAttachAlertLocationLayout;
    }

    @Override // com.google.android.gms.maps.GoogleMap.OnCameraMoveListener
    public final void onCameraMove() {
        this.f$0.lambda$onMapInit$20();
    }
}
