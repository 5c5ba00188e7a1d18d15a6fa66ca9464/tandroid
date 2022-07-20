package org.telegram.ui;

import org.telegram.ui.Components.SlideChooseView;
import org.telegram.ui.DataAutoDownloadActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class DataAutoDownloadActivity$ListAdapter$$ExternalSyntheticLambda0 implements SlideChooseView.Callback {
    public final /* synthetic */ DataAutoDownloadActivity.ListAdapter f$0;

    public /* synthetic */ DataAutoDownloadActivity$ListAdapter$$ExternalSyntheticLambda0(DataAutoDownloadActivity.ListAdapter listAdapter) {
        this.f$0 = listAdapter;
    }

    @Override // org.telegram.ui.Components.SlideChooseView.Callback
    public final void onOptionSelected(int i) {
        this.f$0.lambda$onCreateViewHolder$0(i);
    }

    @Override // org.telegram.ui.Components.SlideChooseView.Callback
    public /* synthetic */ void onTouchEnd() {
        SlideChooseView.Callback.CC.$default$onTouchEnd(this);
    }
}
