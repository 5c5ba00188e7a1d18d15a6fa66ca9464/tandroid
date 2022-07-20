package org.telegram.messenger;

import org.telegram.tgnet.TLRPC$Document;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaController$$ExternalSyntheticLambda7 implements Runnable {
    public final /* synthetic */ AccountInstance f$0;
    public final /* synthetic */ TLRPC$Document f$1;

    public /* synthetic */ MediaController$$ExternalSyntheticLambda7(AccountInstance accountInstance, TLRPC$Document tLRPC$Document) {
        this.f$0 = accountInstance;
        this.f$1 = tLRPC$Document;
    }

    @Override // java.lang.Runnable
    public final void run() {
        MediaController.lambda$playEmojiSound$17(this.f$0, this.f$1);
    }
}
