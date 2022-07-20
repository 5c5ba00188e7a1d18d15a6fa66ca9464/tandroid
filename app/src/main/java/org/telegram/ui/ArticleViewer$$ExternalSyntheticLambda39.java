package org.telegram.ui;

import android.view.KeyEvent;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
/* loaded from: classes3.dex */
public final /* synthetic */ class ArticleViewer$$ExternalSyntheticLambda39 implements ActionBarPopupWindow.OnDispatchKeyEventListener {
    public final /* synthetic */ ArticleViewer f$0;

    public /* synthetic */ ArticleViewer$$ExternalSyntheticLambda39(ArticleViewer articleViewer) {
        this.f$0 = articleViewer;
    }

    @Override // org.telegram.ui.ActionBar.ActionBarPopupWindow.OnDispatchKeyEventListener
    public final void onDispatchKeyEvent(KeyEvent keyEvent) {
        this.f$0.lambda$showPopup$3(keyEvent);
    }
}
