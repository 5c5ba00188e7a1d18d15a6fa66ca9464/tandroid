package org.telegram.messenger;

import org.telegram.messenger.MessagesController;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda152 implements Runnable {
    public final /* synthetic */ MessagesStorage f$0;
    public final /* synthetic */ MessagesController.DialogFilter f$1;

    public /* synthetic */ MessagesStorage$$ExternalSyntheticLambda152(MessagesStorage messagesStorage, MessagesController.DialogFilter dialogFilter) {
        this.f$0 = messagesStorage;
        this.f$1 = dialogFilter;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$deleteDialogFilter$46(this.f$1);
    }
}
