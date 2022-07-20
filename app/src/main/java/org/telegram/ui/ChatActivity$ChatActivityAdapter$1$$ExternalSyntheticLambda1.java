package org.telegram.ui;

import org.telegram.messenger.MessageObject;
import org.telegram.ui.ChatActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$ChatActivityAdapter$1$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ ChatActivity.ChatActivityAdapter.AnonymousClass1 f$0;
    public final /* synthetic */ MessageObject f$1;

    public /* synthetic */ ChatActivity$ChatActivityAdapter$1$$ExternalSyntheticLambda1(ChatActivity.ChatActivityAdapter.AnonymousClass1 anonymousClass1, MessageObject messageObject) {
        this.f$0 = anonymousClass1;
        this.f$1 = messageObject;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$didPressImage$4(this.f$1);
    }
}
