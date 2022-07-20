package org.telegram.ui.Components;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatAttachAlertLocationLayout$$ExternalSyntheticLambda8 implements GoogleMap.OnMarkerClickListener {
    public final /* synthetic */ ChatAttachAlertLocationLayout f$0;

    public /* synthetic */ ChatAttachAlertLocationLayout$$ExternalSyntheticLambda8(ChatAttachAlertLocationLayout chatAttachAlertLocationLayout) {
        this.f$0 = chatAttachAlertLocationLayout;
    }

    @Override // com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
    public final boolean onMarkerClick(Marker marker) {
        boolean lambda$onMapInit$19;
        lambda$onMapInit$19 = this.f$0.lambda$onMapInit$19(marker);
        return lambda$onMapInit$19;
    }
}
