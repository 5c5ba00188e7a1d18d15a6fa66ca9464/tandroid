package org.telegram.messenger;

import org.telegram.messenger.MessagesStorage;
/* loaded from: classes.dex */
public final /* synthetic */ class SendMessagesHelper$$ExternalSyntheticLambda20 implements Runnable {
    public final /* synthetic */ MessagesStorage.StringCallback f$0;

    public /* synthetic */ SendMessagesHelper$$ExternalSyntheticLambda20(MessagesStorage.StringCallback stringCallback) {
        this.f$0 = stringCallback;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.run(null);
    }
}
