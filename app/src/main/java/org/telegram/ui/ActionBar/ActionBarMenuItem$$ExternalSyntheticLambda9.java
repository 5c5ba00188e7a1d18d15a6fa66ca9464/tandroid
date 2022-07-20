package org.telegram.ui.ActionBar;

import android.widget.PopupWindow;
/* loaded from: classes3.dex */
public final /* synthetic */ class ActionBarMenuItem$$ExternalSyntheticLambda9 implements PopupWindow.OnDismissListener {
    public final /* synthetic */ ActionBarMenuItem f$0;

    public /* synthetic */ ActionBarMenuItem$$ExternalSyntheticLambda9(ActionBarMenuItem actionBarMenuItem) {
        this.f$0 = actionBarMenuItem;
    }

    @Override // android.widget.PopupWindow.OnDismissListener
    public final void onDismiss() {
        this.f$0.lambda$toggleSubMenu$10();
    }
}
