package org.telegram.ui;

import org.telegram.messenger.MessagesStorage;
import org.telegram.ui.ChatActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$24$$ExternalSyntheticLambda1 implements MessagesStorage.IntCallback {
    public final /* synthetic */ ChatActivity.AnonymousClass24 f$0;

    public /* synthetic */ ChatActivity$24$$ExternalSyntheticLambda1(ChatActivity.AnonymousClass24 anonymousClass24) {
        this.f$0 = anonymousClass24;
    }

    @Override // org.telegram.messenger.MessagesStorage.IntCallback
    public final void run(int i) {
        this.f$0.lambda$loadLastUnreadMention$0(i);
    }
}
