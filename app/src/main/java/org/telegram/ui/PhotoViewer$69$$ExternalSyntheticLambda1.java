package org.telegram.ui;

import org.telegram.ui.PhotoViewer;
/* loaded from: classes3.dex */
public final /* synthetic */ class PhotoViewer$69$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ PhotoViewer.AnonymousClass69 f$0;
    public final /* synthetic */ PhotoViewer.PlaceProviderObject f$1;

    public /* synthetic */ PhotoViewer$69$$ExternalSyntheticLambda1(PhotoViewer.AnonymousClass69 anonymousClass69, PhotoViewer.PlaceProviderObject placeProviderObject) {
        this.f$0 = anonymousClass69;
        this.f$1 = placeProviderObject;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onPreDraw$2(this.f$1);
    }
}
