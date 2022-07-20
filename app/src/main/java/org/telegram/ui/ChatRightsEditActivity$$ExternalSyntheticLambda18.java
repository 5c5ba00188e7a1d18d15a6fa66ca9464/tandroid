package org.telegram.ui;

import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatRightsEditActivity$$ExternalSyntheticLambda18 implements MessagesController.ErrorDelegate {
    public final /* synthetic */ ChatRightsEditActivity f$0;

    public /* synthetic */ ChatRightsEditActivity$$ExternalSyntheticLambda18(ChatRightsEditActivity chatRightsEditActivity) {
        this.f$0 = chatRightsEditActivity;
    }

    @Override // org.telegram.messenger.MessagesController.ErrorDelegate
    public final boolean run(TLRPC$TL_error tLRPC$TL_error) {
        boolean lambda$onDonePressed$20;
        lambda$onDonePressed$20 = this.f$0.lambda$onDonePressed$20(tLRPC$TL_error);
        return lambda$onDonePressed$20;
    }
}
