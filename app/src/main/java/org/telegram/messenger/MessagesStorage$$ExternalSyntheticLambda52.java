package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda52 implements Runnable {
    public final /* synthetic */ MessagesStorage f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ String f$2;
    public final /* synthetic */ RequestDelegate f$3;

    public /* synthetic */ MessagesStorage$$ExternalSyntheticLambda52(MessagesStorage messagesStorage, int i, String str, RequestDelegate requestDelegate) {
        this.f$0 = messagesStorage;
        this.f$1 = i;
        this.f$2 = str;
        this.f$3 = requestDelegate;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$getBotCache$96(this.f$1, this.f$2, this.f$3);
    }
}
