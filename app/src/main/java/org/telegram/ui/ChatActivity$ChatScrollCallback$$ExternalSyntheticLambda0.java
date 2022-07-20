package org.telegram.ui;

import org.telegram.ui.ChatActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$ChatScrollCallback$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ ChatActivity.ChatScrollCallback f$0;

    public /* synthetic */ ChatActivity$ChatScrollCallback$$ExternalSyntheticLambda0(ChatActivity.ChatScrollCallback chatScrollCallback) {
        this.f$0 = chatScrollCallback;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onEndAnimation$0();
    }
}
