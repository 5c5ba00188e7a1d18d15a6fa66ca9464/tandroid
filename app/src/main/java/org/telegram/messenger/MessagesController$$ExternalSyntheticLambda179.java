package org.telegram.messenger;

import org.telegram.tgnet.TLRPC$TL_updateLangPack;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda179 implements Runnable {
    public final /* synthetic */ MessagesController f$0;
    public final /* synthetic */ TLRPC$TL_updateLangPack f$1;

    public /* synthetic */ MessagesController$$ExternalSyntheticLambda179(MessagesController messagesController, TLRPC$TL_updateLangPack tLRPC$TL_updateLangPack) {
        this.f$0 = messagesController;
        this.f$1 = tLRPC$TL_updateLangPack;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$processUpdateArray$315(this.f$1);
    }
}
