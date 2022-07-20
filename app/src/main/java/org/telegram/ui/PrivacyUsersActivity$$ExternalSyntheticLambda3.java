package org.telegram.ui;

import android.view.View;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public final /* synthetic */ class PrivacyUsersActivity$$ExternalSyntheticLambda3 implements RecyclerListView.OnItemLongClickListener {
    public final /* synthetic */ PrivacyUsersActivity f$0;

    public /* synthetic */ PrivacyUsersActivity$$ExternalSyntheticLambda3(PrivacyUsersActivity privacyUsersActivity) {
        this.f$0 = privacyUsersActivity;
    }

    @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListener
    public final boolean onItemClick(View view, int i) {
        boolean lambda$createView$2;
        lambda$createView$2 = this.f$0.lambda$createView$2(view, i);
        return lambda$createView$2;
    }
}
