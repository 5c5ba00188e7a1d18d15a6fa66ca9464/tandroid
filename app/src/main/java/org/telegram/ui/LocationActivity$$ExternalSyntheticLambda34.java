package org.telegram.ui;

import org.telegram.tgnet.TLRPC$TL_messageMediaVenue;
import org.telegram.ui.Components.AlertsCreator;
/* loaded from: classes3.dex */
public final /* synthetic */ class LocationActivity$$ExternalSyntheticLambda34 implements AlertsCreator.ScheduleDatePickerDelegate {
    public final /* synthetic */ LocationActivity f$0;
    public final /* synthetic */ TLRPC$TL_messageMediaVenue f$1;

    public /* synthetic */ LocationActivity$$ExternalSyntheticLambda34(LocationActivity locationActivity, TLRPC$TL_messageMediaVenue tLRPC$TL_messageMediaVenue) {
        this.f$0 = locationActivity;
        this.f$1 = tLRPC$TL_messageMediaVenue;
    }

    @Override // org.telegram.ui.Components.AlertsCreator.ScheduleDatePickerDelegate
    public final void didSelectDate(boolean z, int i) {
        this.f$0.lambda$createView$19(this.f$1, z, i);
    }
}
