package org.telegram.ui.Components;

import android.location.Location;
import com.google.android.gms.maps.GoogleMap;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatAttachAlertLocationLayout$$ExternalSyntheticLambda9 implements GoogleMap.OnMyLocationChangeListener {
    public final /* synthetic */ ChatAttachAlertLocationLayout f$0;

    public /* synthetic */ ChatAttachAlertLocationLayout$$ExternalSyntheticLambda9(ChatAttachAlertLocationLayout chatAttachAlertLocationLayout) {
        this.f$0 = chatAttachAlertLocationLayout;
    }

    @Override // com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener
    public final void onMyLocationChange(Location location) {
        this.f$0.lambda$onMapInit$18(location);
    }
}
