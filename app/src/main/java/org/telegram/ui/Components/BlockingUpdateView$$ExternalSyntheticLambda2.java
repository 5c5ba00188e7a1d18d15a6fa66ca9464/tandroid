package org.telegram.ui.Components;

import org.telegram.tgnet.TLObject;
/* loaded from: classes3.dex */
public final /* synthetic */ class BlockingUpdateView$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ BlockingUpdateView f$0;
    public final /* synthetic */ TLObject f$1;

    public /* synthetic */ BlockingUpdateView$$ExternalSyntheticLambda2(BlockingUpdateView blockingUpdateView, TLObject tLObject) {
        this.f$0 = blockingUpdateView;
        this.f$1 = tLObject;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$show$2(this.f$1);
    }
}
