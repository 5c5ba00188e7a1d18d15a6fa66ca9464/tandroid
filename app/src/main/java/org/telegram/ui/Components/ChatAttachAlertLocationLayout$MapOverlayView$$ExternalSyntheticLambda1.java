package org.telegram.ui.Components;

import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.ChatAttachAlertLocationLayout;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatAttachAlertLocationLayout$MapOverlayView$$ExternalSyntheticLambda1 implements AlertsCreator.ScheduleDatePickerDelegate {
    public final /* synthetic */ ChatAttachAlertLocationLayout.MapOverlayView f$0;
    public final /* synthetic */ ChatAttachAlertLocationLayout.VenueLocation f$1;

    public /* synthetic */ ChatAttachAlertLocationLayout$MapOverlayView$$ExternalSyntheticLambda1(ChatAttachAlertLocationLayout.MapOverlayView mapOverlayView, ChatAttachAlertLocationLayout.VenueLocation venueLocation) {
        this.f$0 = mapOverlayView;
        this.f$1 = venueLocation;
    }

    @Override // org.telegram.ui.Components.AlertsCreator.ScheduleDatePickerDelegate
    public final void didSelectDate(boolean z, int i) {
        this.f$0.lambda$addInfoView$0(this.f$1, z, i);
    }
}
