package org.telegram.ui;

import org.telegram.tgnet.TLObject;
/* loaded from: classes3.dex */
public final /* synthetic */ class ArticleViewer$$ExternalSyntheticLambda29 implements Runnable {
    public final /* synthetic */ ArticleViewer f$0;
    public final /* synthetic */ TLObject f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ long f$3;

    public /* synthetic */ ArticleViewer$$ExternalSyntheticLambda29(ArticleViewer articleViewer, TLObject tLObject, int i, long j) {
        this.f$0 = articleViewer;
        this.f$1 = tLObject;
        this.f$2 = i;
        this.f$3 = j;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$setParentActivity$10(this.f$1, this.f$2, this.f$3);
    }
}
