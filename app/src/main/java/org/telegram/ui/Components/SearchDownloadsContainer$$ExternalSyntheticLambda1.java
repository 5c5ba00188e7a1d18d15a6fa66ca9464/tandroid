package org.telegram.ui.Components;

import android.view.View;
import org.telegram.ui.ActionBar.BottomSheet;
/* loaded from: classes3.dex */
public final /* synthetic */ class SearchDownloadsContainer$$ExternalSyntheticLambda1 implements View.OnClickListener {
    public final /* synthetic */ SearchDownloadsContainer f$0;
    public final /* synthetic */ BottomSheet f$1;

    public /* synthetic */ SearchDownloadsContainer$$ExternalSyntheticLambda1(SearchDownloadsContainer searchDownloadsContainer, BottomSheet bottomSheet) {
        this.f$0 = searchDownloadsContainer;
        this.f$1 = bottomSheet;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        this.f$0.lambda$showSettingsDialog$7(this.f$1, view);
    }
}
