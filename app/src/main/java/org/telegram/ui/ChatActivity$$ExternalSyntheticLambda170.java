package org.telegram.ui;

import org.telegram.messenger.MessagesController;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda170 implements Runnable {
    public final /* synthetic */ ChatActivity f$0;
    public final /* synthetic */ CharSequence f$1;
    public final /* synthetic */ MessagesController f$2;
    public final /* synthetic */ boolean f$3;

    public /* synthetic */ ChatActivity$$ExternalSyntheticLambda170(ChatActivity chatActivity, CharSequence charSequence, MessagesController messagesController, boolean z) {
        this.f$0 = chatActivity;
        this.f$1 = charSequence;
        this.f$2 = messagesController;
        this.f$3 = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$searchLinks$100(this.f$1, this.f$2, this.f$3);
    }
}
