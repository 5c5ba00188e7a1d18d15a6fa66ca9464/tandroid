package org.telegram.messenger;

import java.util.ArrayList;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda115 implements Runnable {
    public final /* synthetic */ MessagesController f$0;
    public final /* synthetic */ ArrayList f$1;
    public final /* synthetic */ long f$2;

    public /* synthetic */ MessagesController$$ExternalSyntheticLambda115(MessagesController messagesController, ArrayList arrayList, long j) {
        this.f$0 = messagesController;
        this.f$1 = arrayList;
        this.f$2 = j;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$deleteMessagesByPush$295(this.f$1, this.f$2);
    }
}
