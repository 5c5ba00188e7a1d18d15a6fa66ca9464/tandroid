package org.telegram.messenger;

import org.telegram.tgnet.TLRPC$Document;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda88 implements Runnable {
    public final /* synthetic */ MediaDataController f$0;
    public final /* synthetic */ TLRPC$Document f$1;

    public /* synthetic */ MediaDataController$$ExternalSyntheticLambda88(MediaDataController mediaDataController, TLRPC$Document tLRPC$Document) {
        this.f$0 = mediaDataController;
        this.f$1 = tLRPC$Document;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$removeRecentGif$22(this.f$1);
    }
}
