package org.telegram.ui;

import org.telegram.ui.WallpapersListActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class WallpapersListActivity$SearchAdapter$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ WallpapersListActivity.SearchAdapter f$0;
    public final /* synthetic */ String f$1;

    public /* synthetic */ WallpapersListActivity$SearchAdapter$$ExternalSyntheticLambda1(WallpapersListActivity.SearchAdapter searchAdapter, String str) {
        this.f$0 = searchAdapter;
        this.f$1 = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$processSearch$0(this.f$1);
    }
}
