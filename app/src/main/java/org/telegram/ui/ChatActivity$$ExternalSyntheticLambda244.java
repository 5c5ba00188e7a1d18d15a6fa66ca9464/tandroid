package org.telegram.ui;

import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.Components.ReactedUsersListView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda244 implements ReactedUsersListView.OnHeightChangedListener {
    public final /* synthetic */ ActionBarPopupWindow.ActionBarPopupWindowLayout f$0;
    public final /* synthetic */ int[] f$1;

    public /* synthetic */ ChatActivity$$ExternalSyntheticLambda244(ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout, int[] iArr) {
        this.f$0 = actionBarPopupWindowLayout;
        this.f$1 = iArr;
    }

    @Override // org.telegram.ui.Components.ReactedUsersListView.OnHeightChangedListener
    public final void onHeightChanged(ReactedUsersListView reactedUsersListView, int i) {
        ChatActivity.lambda$createMenu$159(this.f$0, this.f$1, reactedUsersListView, i);
    }
}
