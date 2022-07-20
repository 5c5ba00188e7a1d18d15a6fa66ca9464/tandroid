package org.telegram.messenger;

import org.telegram.messenger.MessagesStorage;
/* loaded from: classes.dex */
public final /* synthetic */ class SendMessagesHelper$$ExternalSyntheticLambda19 implements Runnable {
    public final /* synthetic */ MessagesStorage.LongCallback f$0;

    public /* synthetic */ SendMessagesHelper$$ExternalSyntheticLambda19(MessagesStorage.LongCallback longCallback) {
        this.f$0 = longCallback;
    }

    @Override // java.lang.Runnable
    public final void run() {
        SendMessagesHelper.lambda$prepareImportHistory$67(this.f$0);
    }
}
