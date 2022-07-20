package org.telegram.ui;

import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ContentPreviewViewer$$ExternalSyntheticLambda6 implements Runnable {
    public final /* synthetic */ ContentPreviewViewer f$0;
    public final /* synthetic */ RecyclerListView f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ Theme.ResourcesProvider f$3;

    public /* synthetic */ ContentPreviewViewer$$ExternalSyntheticLambda6(ContentPreviewViewer contentPreviewViewer, RecyclerListView recyclerListView, int i, Theme.ResourcesProvider resourcesProvider) {
        this.f$0 = contentPreviewViewer;
        this.f$1 = recyclerListView;
        this.f$2 = i;
        this.f$3 = resourcesProvider;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onInterceptTouchEvent$3(this.f$1, this.f$2, this.f$3);
    }
}
