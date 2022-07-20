package org.telegram.ui.Components;

import android.view.View;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatAttachAlertPhotoLayout$$ExternalSyntheticLambda15 implements RecyclerListView.OnItemClickListener {
    public final /* synthetic */ ChatAttachAlertPhotoLayout f$0;
    public final /* synthetic */ Theme.ResourcesProvider f$1;

    public /* synthetic */ ChatAttachAlertPhotoLayout$$ExternalSyntheticLambda15(ChatAttachAlertPhotoLayout chatAttachAlertPhotoLayout, Theme.ResourcesProvider resourcesProvider) {
        this.f$0 = chatAttachAlertPhotoLayout;
        this.f$1 = resourcesProvider;
    }

    @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
    public final void onItemClick(View view, int i) {
        this.f$0.lambda$new$1(this.f$1, view, i);
    }
}
