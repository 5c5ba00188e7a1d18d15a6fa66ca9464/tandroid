package org.telegram.ui;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$User;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda188 implements Runnable {
    public final /* synthetic */ ChatActivity f$0;
    public final /* synthetic */ TLObject f$1;
    public final /* synthetic */ TLRPC$User f$2;

    public /* synthetic */ ChatActivity$$ExternalSyntheticLambda188(ChatActivity chatActivity, TLObject tLObject, TLRPC$User tLRPC$User) {
        this.f$0 = chatActivity;
        this.f$1 = tLObject;
        this.f$2 = tLRPC$User;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onTransitionAnimationEnd$134(this.f$1, this.f$2);
    }
}
