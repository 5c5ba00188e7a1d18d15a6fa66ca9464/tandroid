package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$User;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda228 implements RequestDelegate {
    public final /* synthetic */ ChatActivity f$0;
    public final /* synthetic */ TLRPC$User f$1;

    public /* synthetic */ ChatActivity$$ExternalSyntheticLambda228(ChatActivity chatActivity, TLRPC$User tLRPC$User) {
        this.f$0 = chatActivity;
        this.f$1 = tLRPC$User;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$onTransitionAnimationEnd$132(this.f$1, tLObject, tLRPC$TL_error);
    }
}
