package org.telegram.messenger;

import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes.dex */
public final /* synthetic */ class AndroidUtilities$$ExternalSyntheticLambda14 implements RecyclerListView.IntReturnCallback {
    public final /* synthetic */ BaseFragment f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ RecyclerListView f$2;

    public /* synthetic */ AndroidUtilities$$ExternalSyntheticLambda14(BaseFragment baseFragment, String str, RecyclerListView recyclerListView) {
        this.f$0 = baseFragment;
        this.f$1 = str;
        this.f$2 = recyclerListView;
    }

    @Override // org.telegram.ui.Components.RecyclerListView.IntReturnCallback
    public final int run() {
        int lambda$scrollToFragmentRow$13;
        lambda$scrollToFragmentRow$13 = AndroidUtilities.lambda$scrollToFragmentRow$13(this.f$0, this.f$1, this.f$2);
        return lambda$scrollToFragmentRow$13;
    }
}
