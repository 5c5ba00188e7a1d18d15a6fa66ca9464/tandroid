package org.telegram.ui;

import org.telegram.ui.PhotoViewer;
/* loaded from: classes3.dex */
public final /* synthetic */ class PhotoViewer$12$$ExternalSyntheticLambda7 implements Runnable {
    public final /* synthetic */ PhotoViewer.AnonymousClass12 f$0;
    public final /* synthetic */ boolean f$1;

    public /* synthetic */ PhotoViewer$12$$ExternalSyntheticLambda7(PhotoViewer.AnonymousClass12 anonymousClass12, boolean z) {
        this.f$0 = anonymousClass12;
        this.f$1 = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onItemClick$0(this.f$1);
    }
}
