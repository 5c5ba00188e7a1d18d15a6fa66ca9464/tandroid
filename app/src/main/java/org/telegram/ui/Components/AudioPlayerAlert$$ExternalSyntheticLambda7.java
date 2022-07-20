package org.telegram.ui.Components;

import org.telegram.ui.ActionBar.ActionBarMenuItem;
/* loaded from: classes3.dex */
public final /* synthetic */ class AudioPlayerAlert$$ExternalSyntheticLambda7 implements ActionBarMenuItem.ActionBarMenuItemDelegate {
    public final /* synthetic */ AudioPlayerAlert f$0;

    public /* synthetic */ AudioPlayerAlert$$ExternalSyntheticLambda7(AudioPlayerAlert audioPlayerAlert) {
        this.f$0 = audioPlayerAlert;
    }

    @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemDelegate
    public final void onItemClick(int i) {
        this.f$0.onSubItemClick(i);
    }
}
