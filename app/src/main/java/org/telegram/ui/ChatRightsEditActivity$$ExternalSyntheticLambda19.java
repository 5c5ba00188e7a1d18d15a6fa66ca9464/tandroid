package org.telegram.ui;

import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatRightsEditActivity$$ExternalSyntheticLambda19 implements MessagesController.ErrorDelegate {
    public final /* synthetic */ ChatRightsEditActivity f$0;

    public /* synthetic */ ChatRightsEditActivity$$ExternalSyntheticLambda19(ChatRightsEditActivity chatRightsEditActivity) {
        this.f$0 = chatRightsEditActivity;
    }

    @Override // org.telegram.messenger.MessagesController.ErrorDelegate
    public final boolean run(TLRPC$TL_error tLRPC$TL_error) {
        boolean lambda$onDonePressed$19;
        lambda$onDonePressed$19 = this.f$0.lambda$onDonePressed$19(tLRPC$TL_error);
        return lambda$onDonePressed$19;
    }
}
