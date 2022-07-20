package org.telegram.ui;

import org.telegram.messenger.MessagesStorage;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda213 implements MessagesStorage.IntCallback {
    public final /* synthetic */ ChatActivity f$0;

    public /* synthetic */ ChatActivity$$ExternalSyntheticLambda213(ChatActivity chatActivity) {
        this.f$0 = chatActivity;
    }

    @Override // org.telegram.messenger.MessagesStorage.IntCallback
    public final void run(int i) {
        this.f$0.lambda$createView$32(i);
    }
}
