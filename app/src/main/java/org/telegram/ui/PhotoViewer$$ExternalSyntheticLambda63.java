package org.telegram.ui;

import org.telegram.ui.Components.ShareAlert;
/* loaded from: classes3.dex */
public final /* synthetic */ class PhotoViewer$$ExternalSyntheticLambda63 implements Runnable {
    public final /* synthetic */ PhotoViewer f$0;
    public final /* synthetic */ ShareAlert f$1;

    public /* synthetic */ PhotoViewer$$ExternalSyntheticLambda63(PhotoViewer photoViewer, ShareAlert shareAlert) {
        this.f$0 = photoViewer;
        this.f$1 = shareAlert;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$showShareAlert$43(this.f$1);
    }
}
