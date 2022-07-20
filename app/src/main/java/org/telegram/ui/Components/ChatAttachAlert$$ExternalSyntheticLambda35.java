package org.telegram.ui.Components;

import android.view.View;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatAttachAlert$$ExternalSyntheticLambda35 implements RecyclerListView.OnItemClickListener {
    public final /* synthetic */ ChatAttachAlert f$0;
    public final /* synthetic */ Theme.ResourcesProvider f$1;

    public /* synthetic */ ChatAttachAlert$$ExternalSyntheticLambda35(ChatAttachAlert chatAttachAlert, Theme.ResourcesProvider resourcesProvider) {
        this.f$0 = chatAttachAlert;
        this.f$1 = resourcesProvider;
    }

    @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
    public final void onItemClick(View view, int i) {
        this.f$0.lambda$new$7(this.f$1, view, i);
    }
}
