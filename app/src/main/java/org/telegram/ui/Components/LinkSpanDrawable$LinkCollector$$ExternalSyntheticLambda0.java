package org.telegram.ui.Components;

import org.telegram.ui.Components.LinkSpanDrawable;
/* loaded from: classes3.dex */
public final /* synthetic */ class LinkSpanDrawable$LinkCollector$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ LinkSpanDrawable.LinkCollector f$0;
    public final /* synthetic */ LinkSpanDrawable f$1;

    public /* synthetic */ LinkSpanDrawable$LinkCollector$$ExternalSyntheticLambda0(LinkSpanDrawable.LinkCollector linkCollector, LinkSpanDrawable linkSpanDrawable) {
        this.f$0 = linkCollector;
        this.f$1 = linkSpanDrawable;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$removeLink$0(this.f$1);
    }
}
