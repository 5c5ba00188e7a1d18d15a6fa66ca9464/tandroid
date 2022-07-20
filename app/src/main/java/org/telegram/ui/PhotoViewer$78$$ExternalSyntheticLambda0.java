package org.telegram.ui;

import org.telegram.ui.PhotoViewer;
/* loaded from: classes3.dex */
public final /* synthetic */ class PhotoViewer$78$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ PhotoViewer.AnonymousClass78 f$0;
    public final /* synthetic */ Runnable f$1;
    public final /* synthetic */ int[] f$2;
    public final /* synthetic */ int f$3;

    public /* synthetic */ PhotoViewer$78$$ExternalSyntheticLambda0(PhotoViewer.AnonymousClass78 anonymousClass78, Runnable runnable, int[] iArr, int i) {
        this.f$0 = anonymousClass78;
        this.f$1 = runnable;
        this.f$2 = iArr;
        this.f$3 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$run$0(this.f$1, this.f$2, this.f$3);
    }
}
