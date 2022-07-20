package org.telegram.messenger;

import org.telegram.tgnet.TLRPC$Document;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda126 implements Runnable {
    public final /* synthetic */ TLRPC$Document f$0;

    public /* synthetic */ MediaDataController$$ExternalSyntheticLambda126(TLRPC$Document tLRPC$Document) {
        this.f$0 = tLRPC$Document;
    }

    @Override // java.lang.Runnable
    public final void run() {
        MediaDataController.lambda$addRecentGif$24(this.f$0);
    }
}
