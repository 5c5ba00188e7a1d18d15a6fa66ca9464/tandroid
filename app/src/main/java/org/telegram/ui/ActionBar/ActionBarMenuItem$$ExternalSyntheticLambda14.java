package org.telegram.ui.ActionBar;

import android.view.KeyEvent;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
/* loaded from: classes3.dex */
public final /* synthetic */ class ActionBarMenuItem$$ExternalSyntheticLambda14 implements ActionBarPopupWindow.OnDispatchKeyEventListener {
    public final /* synthetic */ ActionBarMenuItem f$0;

    public /* synthetic */ ActionBarMenuItem$$ExternalSyntheticLambda14(ActionBarMenuItem actionBarMenuItem) {
        this.f$0 = actionBarMenuItem;
    }

    @Override // org.telegram.ui.ActionBar.ActionBarPopupWindow.OnDispatchKeyEventListener
    public final void onDispatchKeyEvent(KeyEvent keyEvent) {
        this.f$0.lambda$createPopupLayout$2(keyEvent);
    }
}
