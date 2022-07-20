package org.telegram.ui;

import android.view.View;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public final /* synthetic */ class PhotoViewer$$ExternalSyntheticLambda78 implements RecyclerListView.OnItemLongClickListener {
    public final /* synthetic */ PhotoViewer f$0;
    public final /* synthetic */ Theme.ResourcesProvider f$1;

    public /* synthetic */ PhotoViewer$$ExternalSyntheticLambda78(PhotoViewer photoViewer, Theme.ResourcesProvider resourcesProvider) {
        this.f$0 = photoViewer;
        this.f$1 = resourcesProvider;
    }

    @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListener
    public final boolean onItemClick(View view, int i) {
        boolean lambda$setParentActivity$41;
        lambda$setParentActivity$41 = this.f$0.lambda$setParentActivity$41(this.f$1, view, i);
        return lambda$setParentActivity$41;
    }
}
