package org.telegram.ui;

import org.telegram.ui.Components.LinkSpanDrawable;
/* loaded from: classes4.dex */
public final /* synthetic */ class PhotoViewer$CaptionTextView$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ LinkSpanDrawable.LinkCollector f$0;

    public /* synthetic */ PhotoViewer$CaptionTextView$$ExternalSyntheticLambda1(LinkSpanDrawable.LinkCollector linkCollector) {
        this.f$0 = linkCollector;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.clear();
    }
}
