package org.telegram.ui.Components;

import com.google.android.gms.maps.GoogleMap;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatAttachAlertLocationLayout$$ExternalSyntheticLambda6 implements GoogleMap.OnCameraMoveStartedListener {
    public final /* synthetic */ ChatAttachAlertLocationLayout f$0;

    public /* synthetic */ ChatAttachAlertLocationLayout$$ExternalSyntheticLambda6(ChatAttachAlertLocationLayout chatAttachAlertLocationLayout) {
        this.f$0 = chatAttachAlertLocationLayout;
    }

    @Override // com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener
    public final void onCameraMoveStarted(int i) {
        this.f$0.lambda$onMapInit$17(i);
    }
}
