package org.telegram.ui;

import android.content.Context;
import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda211 implements MessagesController.ErrorDelegate {
    public final /* synthetic */ ChatActivity f$0;
    public final /* synthetic */ Context f$1;

    public /* synthetic */ ChatActivity$$ExternalSyntheticLambda211(ChatActivity chatActivity, Context context) {
        this.f$0 = chatActivity;
        this.f$1 = context;
    }

    @Override // org.telegram.messenger.MessagesController.ErrorDelegate
    public final boolean run(TLRPC$TL_error tLRPC$TL_error) {
        boolean lambda$createView$70;
        lambda$createView$70 = this.f$0.lambda$createView$70(this.f$1, tLRPC$TL_error);
        return lambda$createView$70;
    }
}
