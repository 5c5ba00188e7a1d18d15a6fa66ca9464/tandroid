package org.telegram.messenger;

import java.util.ArrayList;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda138 implements Runnable {
    public final /* synthetic */ MessagesStorage f$0;
    public final /* synthetic */ ArrayList f$1;

    public /* synthetic */ MessagesStorage$$ExternalSyntheticLambda138(MessagesStorage messagesStorage, ArrayList arrayList) {
        this.f$0 = messagesStorage;
        this.f$1 = arrayList;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$deleteFromDownloadQueue$146(this.f$1);
    }
}
