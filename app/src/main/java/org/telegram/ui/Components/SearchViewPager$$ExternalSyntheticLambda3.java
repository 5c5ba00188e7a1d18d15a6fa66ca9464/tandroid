package org.telegram.ui.Components;

import java.util.ArrayList;
import org.telegram.ui.DialogsActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class SearchViewPager$$ExternalSyntheticLambda3 implements DialogsActivity.DialogsActivityDelegate {
    public final /* synthetic */ SearchViewPager f$0;

    public /* synthetic */ SearchViewPager$$ExternalSyntheticLambda3(SearchViewPager searchViewPager) {
        this.f$0 = searchViewPager;
    }

    @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
    public final void didSelectDialogs(DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z) {
        this.f$0.lambda$onActionBarItemClick$3(dialogsActivity, arrayList, charSequence, z);
    }
}
