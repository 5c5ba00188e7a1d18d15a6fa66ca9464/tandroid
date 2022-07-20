package org.telegram.ui.Components;

import org.telegram.tgnet.TLRPC$TL_messageMediaVenue;
import org.telegram.ui.Components.AlertsCreator;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatAttachAlertLocationLayout$$ExternalSyntheticLambda25 implements AlertsCreator.ScheduleDatePickerDelegate {
    public final /* synthetic */ ChatAttachAlertLocationLayout f$0;
    public final /* synthetic */ TLRPC$TL_messageMediaVenue f$1;

    public /* synthetic */ ChatAttachAlertLocationLayout$$ExternalSyntheticLambda25(ChatAttachAlertLocationLayout chatAttachAlertLocationLayout, TLRPC$TL_messageMediaVenue tLRPC$TL_messageMediaVenue) {
        this.f$0 = chatAttachAlertLocationLayout;
        this.f$1 = tLRPC$TL_messageMediaVenue;
    }

    @Override // org.telegram.ui.Components.AlertsCreator.ScheduleDatePickerDelegate
    public final void didSelectDate(boolean z, int i) {
        this.f$0.lambda$new$14(this.f$1, z, i);
    }
}
