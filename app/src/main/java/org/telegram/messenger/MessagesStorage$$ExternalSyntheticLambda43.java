package org.telegram.messenger;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda43 implements Runnable {
    public final /* synthetic */ MessagesStorage f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ long f$2;

    public /* synthetic */ MessagesStorage$$ExternalSyntheticLambda43(MessagesStorage messagesStorage, int i, long j) {
        this.f$0 = messagesStorage;
        this.f$1 = i;
        this.f$2 = j;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$setDialogPinned$189(this.f$1, this.f$2);
    }
}
