package org.telegram.ui;

import android.view.View;
/* loaded from: classes3.dex */
public final /* synthetic */ class PhotoViewer$$ExternalSyntheticLambda69 implements Runnable {
    public final /* synthetic */ boolean f$0;
    public final /* synthetic */ View f$1;

    public /* synthetic */ PhotoViewer$$ExternalSyntheticLambda69(boolean z, View view) {
        this.f$0 = z;
        this.f$1 = view;
    }

    @Override // java.lang.Runnable
    public final void run() {
        PhotoViewer.lambda$setItemVisible$66(this.f$0, this.f$1);
    }
}
