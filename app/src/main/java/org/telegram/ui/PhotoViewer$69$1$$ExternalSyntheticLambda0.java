package org.telegram.ui;

import org.telegram.ui.PhotoViewer;
/* loaded from: classes3.dex */
public final /* synthetic */ class PhotoViewer$69$1$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ PhotoViewer.AnonymousClass69.AnonymousClass1 f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ PhotoViewer$69$1$$ExternalSyntheticLambda0(PhotoViewer.AnonymousClass69.AnonymousClass1 anonymousClass1, int i) {
        this.f$0 = anonymousClass1;
        this.f$1 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onAnimationEnd$0(this.f$1);
    }
}
