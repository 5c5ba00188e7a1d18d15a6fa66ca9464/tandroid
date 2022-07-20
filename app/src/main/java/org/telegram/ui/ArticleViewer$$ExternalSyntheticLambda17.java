package org.telegram.ui;

import org.telegram.tgnet.TLRPC$Chat;
/* loaded from: classes3.dex */
public final /* synthetic */ class ArticleViewer$$ExternalSyntheticLambda17 implements Runnable {
    public final /* synthetic */ int f$0;
    public final /* synthetic */ TLRPC$Chat f$1;

    public /* synthetic */ ArticleViewer$$ExternalSyntheticLambda17(int i, TLRPC$Chat tLRPC$Chat) {
        this.f$0 = i;
        this.f$1 = tLRPC$Chat;
    }

    @Override // java.lang.Runnable
    public final void run() {
        ArticleViewer.lambda$joinChannel$42(this.f$0, this.f$1);
    }
}
