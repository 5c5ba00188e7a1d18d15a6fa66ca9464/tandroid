package org.telegram.messenger;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda184 implements Runnable {
    public final /* synthetic */ MessagesStorage f$0;
    public final /* synthetic */ boolean f$1;

    public /* synthetic */ MessagesStorage$$ExternalSyntheticLambda184(MessagesStorage messagesStorage, boolean z) {
        this.f$0 = messagesStorage;
        this.f$1 = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$cleanup$6(this.f$1);
    }
}
