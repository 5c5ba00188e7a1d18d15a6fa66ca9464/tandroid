package org.telegram.messenger;

import org.telegram.tgnet.TLObject;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda89 implements Runnable {
    public final /* synthetic */ MessagesStorage f$0;
    public final /* synthetic */ long f$1;
    public final /* synthetic */ long f$2;
    public final /* synthetic */ TLObject f$3;

    public /* synthetic */ MessagesStorage$$ExternalSyntheticLambda89(MessagesStorage messagesStorage, long j, long j2, TLObject tLObject) {
        this.f$0 = messagesStorage;
        this.f$1 = j;
        this.f$2 = j2;
        this.f$3 = tLObject;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$loadPendingTasks$27(this.f$1, this.f$2, this.f$3);
    }
}
