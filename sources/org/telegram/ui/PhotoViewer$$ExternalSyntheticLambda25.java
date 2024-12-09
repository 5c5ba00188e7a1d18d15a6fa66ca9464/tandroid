package org.telegram.ui;

/* loaded from: classes4.dex */
public final /* synthetic */ class PhotoViewer$$ExternalSyntheticLambda25 implements Runnable {
    public final /* synthetic */ PhotoViewer f$0;

    public /* synthetic */ PhotoViewer$$ExternalSyntheticLambda25(PhotoViewer photoViewer) {
        this.f$0 = photoViewer;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.invalidateBlur();
    }
}
