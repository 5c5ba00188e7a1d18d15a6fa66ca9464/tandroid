package org.telegram.ui;

import org.telegram.ui.Components.LinkSpanDrawable;
import org.telegram.ui.PhotoViewer;
/* loaded from: classes3.dex */
public final /* synthetic */ class PhotoViewer$38$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ PhotoViewer.AnonymousClass38 f$0;
    public final /* synthetic */ LinkSpanDrawable f$1;

    public /* synthetic */ PhotoViewer$38$$ExternalSyntheticLambda1(PhotoViewer.AnonymousClass38 anonymousClass38, LinkSpanDrawable linkSpanDrawable) {
        this.f$0 = anonymousClass38;
        this.f$1 = linkSpanDrawable;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onTouchEvent$1(this.f$1);
    }
}
