package org.telegram.ui.ActionBar;

import android.view.KeyEvent;
import android.view.View;
/* loaded from: classes3.dex */
public final /* synthetic */ class ActionBarMenuItem$$ExternalSyntheticLambda7 implements View.OnKeyListener {
    public final /* synthetic */ ActionBarMenuItem f$0;

    public /* synthetic */ ActionBarMenuItem$$ExternalSyntheticLambda7(ActionBarMenuItem actionBarMenuItem) {
        this.f$0 = actionBarMenuItem;
    }

    @Override // android.view.View.OnKeyListener
    public final boolean onKey(View view, int i, KeyEvent keyEvent) {
        boolean lambda$toggleSubMenu$9;
        lambda$toggleSubMenu$9 = this.f$0.lambda$toggleSubMenu$9(view, i, keyEvent);
        return lambda$toggleSubMenu$9;
    }
}
