package org.telegram.ui;

import android.view.View;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public final /* synthetic */ class PhotoViewer$$ExternalSyntheticLambda76 implements RecyclerListView.OnItemClickListener {
    public final /* synthetic */ PhotoViewer f$0;

    public /* synthetic */ PhotoViewer$$ExternalSyntheticLambda76(PhotoViewer photoViewer) {
        this.f$0 = photoViewer;
    }

    @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
    public final void onItemClick(View view, int i) {
        this.f$0.lambda$setParentActivity$38(view, i);
    }
}
