package org.telegram.ui;

import org.telegram.tgnet.TLObject;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda183 implements Runnable {
    public final /* synthetic */ ChatActivity f$0;
    public final /* synthetic */ TLObject f$1;

    public /* synthetic */ ChatActivity$$ExternalSyntheticLambda183(ChatActivity chatActivity, TLObject tLObject) {
        this.f$0 = chatActivity;
        this.f$1 = tLObject;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onTransitionAnimationEnd$136(this.f$1);
    }
}
