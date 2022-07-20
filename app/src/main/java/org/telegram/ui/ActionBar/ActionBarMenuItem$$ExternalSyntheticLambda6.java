package org.telegram.ui.ActionBar;

import android.view.View;
/* loaded from: classes3.dex */
public final /* synthetic */ class ActionBarMenuItem$$ExternalSyntheticLambda6 implements View.OnClickListener {
    public final /* synthetic */ ActionBarMenuSubItem f$0;

    public /* synthetic */ ActionBarMenuItem$$ExternalSyntheticLambda6(ActionBarMenuSubItem actionBarMenuSubItem) {
        this.f$0 = actionBarMenuSubItem;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        this.f$0.openSwipeBack();
    }
}
