package org.telegram.ui.Components;

import org.telegram.ui.ActionBar.ActionBarMenuItem;
/* loaded from: classes3.dex */
public final /* synthetic */ class StickersAlert$$ExternalSyntheticLambda33 implements ActionBarMenuItem.ActionBarMenuItemDelegate {
    public final /* synthetic */ StickersAlert f$0;

    public /* synthetic */ StickersAlert$$ExternalSyntheticLambda33(StickersAlert stickersAlert) {
        this.f$0 = stickersAlert;
    }

    @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemDelegate
    public final void onItemClick(int i) {
        this.f$0.onSubItemClick(i);
    }
}
