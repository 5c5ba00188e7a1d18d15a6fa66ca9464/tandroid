package org.telegram.messenger;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda127 implements Runnable {
    public final /* synthetic */ MessagesController f$0;
    public final /* synthetic */ MessageObject f$1;

    public /* synthetic */ MessagesController$$ExternalSyntheticLambda127(MessagesController messagesController, MessageObject messageObject) {
        this.f$0 = messagesController;
        this.f$1 = messageObject;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$addToViewsQueue$189(this.f$1);
    }
}
