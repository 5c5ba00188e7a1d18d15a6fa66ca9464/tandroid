package org.telegram.ui;

import org.telegram.ui.Components.AlertsCreator;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda237 implements AlertsCreator.ScheduleDatePickerDelegate {
    public final /* synthetic */ ChatActivity f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ ChatActivity$$ExternalSyntheticLambda237(ChatActivity chatActivity, Object obj) {
        this.f$0 = chatActivity;
        this.f$1 = obj;
    }

    @Override // org.telegram.ui.Components.AlertsCreator.ScheduleDatePickerDelegate
    public final void didSelectDate(boolean z, int i) {
        this.f$0.lambda$createView$44(this.f$1, z, i);
    }
}
