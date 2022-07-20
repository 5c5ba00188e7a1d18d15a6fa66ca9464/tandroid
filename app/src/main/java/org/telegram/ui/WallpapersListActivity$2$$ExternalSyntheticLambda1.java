package org.telegram.ui;

import org.telegram.ui.WallpapersListActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class WallpapersListActivity$2$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ WallpapersListActivity.AnonymousClass2 f$0;
    public final /* synthetic */ int[] f$1;

    public /* synthetic */ WallpapersListActivity$2$$ExternalSyntheticLambda1(WallpapersListActivity.AnonymousClass2 anonymousClass2, int[] iArr) {
        this.f$0 = anonymousClass2;
        this.f$1 = iArr;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onItemClick$0(this.f$1);
    }
}
