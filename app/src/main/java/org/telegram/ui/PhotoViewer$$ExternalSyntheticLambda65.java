package org.telegram.ui;

import org.telegram.ui.PhotoViewer;
/* loaded from: classes3.dex */
public final /* synthetic */ class PhotoViewer$$ExternalSyntheticLambda65 implements Runnable {
    public final /* synthetic */ PhotoViewer f$0;
    public final /* synthetic */ PhotoViewer.PlaceProviderObject f$1;

    public /* synthetic */ PhotoViewer$$ExternalSyntheticLambda65(PhotoViewer photoViewer, PhotoViewer.PlaceProviderObject placeProviderObject) {
        this.f$0 = photoViewer;
        this.f$1 = placeProviderObject;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$closePhoto$73(this.f$1);
    }
}
