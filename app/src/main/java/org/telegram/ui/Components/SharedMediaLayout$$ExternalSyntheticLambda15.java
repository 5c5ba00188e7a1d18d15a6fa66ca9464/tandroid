package org.telegram.ui.Components;

import android.view.View;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.SharedMediaLayout;
/* loaded from: classes3.dex */
public final /* synthetic */ class SharedMediaLayout$$ExternalSyntheticLambda15 implements RecyclerListView.OnItemClickListener {
    public final /* synthetic */ SharedMediaLayout f$0;
    public final /* synthetic */ SharedMediaLayout.MediaPage f$1;

    public /* synthetic */ SharedMediaLayout$$ExternalSyntheticLambda15(SharedMediaLayout sharedMediaLayout, SharedMediaLayout.MediaPage mediaPage) {
        this.f$0 = sharedMediaLayout;
        this.f$1 = mediaPage;
    }

    @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
    public final void onItemClick(View view, int i) {
        this.f$0.lambda$new$6(this.f$1, view, i);
    }
}
