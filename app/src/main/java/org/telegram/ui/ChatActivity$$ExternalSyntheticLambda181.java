package org.telegram.ui;

import java.util.concurrent.CountDownLatch;
import org.telegram.messenger.MessagesStorage;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda181 implements Runnable {
    public final /* synthetic */ ChatActivity f$0;
    public final /* synthetic */ MessagesStorage f$1;
    public final /* synthetic */ long f$2;
    public final /* synthetic */ CountDownLatch f$3;

    public /* synthetic */ ChatActivity$$ExternalSyntheticLambda181(ChatActivity chatActivity, MessagesStorage messagesStorage, long j, CountDownLatch countDownLatch) {
        this.f$0 = chatActivity;
        this.f$1 = messagesStorage;
        this.f$2 = j;
        this.f$3 = countDownLatch;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onFragmentCreate$6(this.f$1, this.f$2, this.f$3);
    }
}
