package org.telegram.ui;

import org.telegram.tgnet.TLRPC$BotInlineResult;
import org.telegram.ui.Components.AlertsCreator;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda240 implements AlertsCreator.ScheduleDatePickerDelegate {
    public final /* synthetic */ ChatActivity f$0;
    public final /* synthetic */ TLRPC$BotInlineResult f$1;

    public /* synthetic */ ChatActivity$$ExternalSyntheticLambda240(ChatActivity chatActivity, TLRPC$BotInlineResult tLRPC$BotInlineResult) {
        this.f$0 = chatActivity;
        this.f$1 = tLRPC$BotInlineResult;
    }

    @Override // org.telegram.ui.Components.AlertsCreator.ScheduleDatePickerDelegate
    public final void didSelectDate(boolean z, int i) {
        this.f$0.lambda$createView$45(this.f$1, z, i);
    }
}
