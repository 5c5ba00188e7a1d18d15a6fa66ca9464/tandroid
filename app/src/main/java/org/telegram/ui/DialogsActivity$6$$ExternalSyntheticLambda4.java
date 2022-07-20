package org.telegram.ui;

import android.view.KeyEvent;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.DialogsActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class DialogsActivity$6$$ExternalSyntheticLambda4 implements ActionBarPopupWindow.OnDispatchKeyEventListener {
    public final /* synthetic */ DialogsActivity.AnonymousClass6 f$0;

    public /* synthetic */ DialogsActivity$6$$ExternalSyntheticLambda4(DialogsActivity.AnonymousClass6 anonymousClass6) {
        this.f$0 = anonymousClass6;
    }

    @Override // org.telegram.ui.ActionBar.ActionBarPopupWindow.OnDispatchKeyEventListener
    public final void onDispatchKeyEvent(KeyEvent keyEvent) {
        this.f$0.lambda$didSelectTab$3(keyEvent);
    }
}
