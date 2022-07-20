package org.telegram.ui;

import android.view.View;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public final /* synthetic */ class DataUsageActivity$$ExternalSyntheticLambda2 implements RecyclerListView.OnItemClickListener {
    public final /* synthetic */ DataUsageActivity f$0;
    public final /* synthetic */ RecyclerListView f$1;

    public /* synthetic */ DataUsageActivity$$ExternalSyntheticLambda2(DataUsageActivity dataUsageActivity, RecyclerListView recyclerListView) {
        this.f$0 = dataUsageActivity;
        this.f$1 = recyclerListView;
    }

    @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
    public final void onItemClick(View view, int i) {
        this.f$0.lambda$createView$2(this.f$1, view, i);
    }
}
