package org.telegram.ui;

import android.view.View;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.Components.ReactedUsersListView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda91 implements View.OnClickListener {
    public final /* synthetic */ ReactedUsersListView f$0;
    public final /* synthetic */ ActionBarPopupWindow.ActionBarPopupWindowLayout f$1;
    public final /* synthetic */ int[] f$2;

    public /* synthetic */ ChatActivity$$ExternalSyntheticLambda91(ReactedUsersListView reactedUsersListView, ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout, int[] iArr) {
        this.f$0 = reactedUsersListView;
        this.f$1 = actionBarPopupWindowLayout;
        this.f$2 = iArr;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        ChatActivity.lambda$createMenu$160(this.f$0, this.f$1, this.f$2, view);
    }
}
