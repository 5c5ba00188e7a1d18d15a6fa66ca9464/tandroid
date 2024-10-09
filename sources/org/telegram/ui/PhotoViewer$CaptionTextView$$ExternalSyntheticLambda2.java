package org.telegram.ui;

import org.telegram.ui.Components.LinkSpanDrawable;

/* loaded from: classes4.dex */
public final /* synthetic */ class PhotoViewer$CaptionTextView$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ LinkSpanDrawable.LinkCollector f$0;

    public /* synthetic */ PhotoViewer$CaptionTextView$$ExternalSyntheticLambda2(LinkSpanDrawable.LinkCollector linkCollector) {
        this.f$0 = linkCollector;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.clear();
    }
}
