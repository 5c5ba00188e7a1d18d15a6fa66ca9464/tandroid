package org.telegram.ui.Components;

import android.view.KeyEvent;
import android.view.View;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
/* loaded from: classes3.dex */
public final /* synthetic */ class AlertsCreator$$ExternalSyntheticLambda74 implements View.OnKeyListener {
    public final /* synthetic */ ActionBarPopupWindow f$0;

    public /* synthetic */ AlertsCreator$$ExternalSyntheticLambda74(ActionBarPopupWindow actionBarPopupWindow) {
        this.f$0 = actionBarPopupWindow;
    }

    @Override // android.view.View.OnKeyListener
    public final boolean onKey(View view, int i, KeyEvent keyEvent) {
        boolean lambda$showPopupMenu$127;
        lambda$showPopupMenu$127 = AlertsCreator.lambda$showPopupMenu$127(this.f$0, view, i, keyEvent);
        return lambda$showPopupMenu$127;
    }
}
