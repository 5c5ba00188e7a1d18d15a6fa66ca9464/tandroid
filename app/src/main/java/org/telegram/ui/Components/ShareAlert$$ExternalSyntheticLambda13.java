package org.telegram.ui.Components;

import android.view.KeyEvent;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
/* loaded from: classes3.dex */
public final /* synthetic */ class ShareAlert$$ExternalSyntheticLambda13 implements ActionBarPopupWindow.OnDispatchKeyEventListener {
    public final /* synthetic */ ShareAlert f$0;

    public /* synthetic */ ShareAlert$$ExternalSyntheticLambda13(ShareAlert shareAlert) {
        this.f$0 = shareAlert;
    }

    @Override // org.telegram.ui.ActionBar.ActionBarPopupWindow.OnDispatchKeyEventListener
    public final void onDispatchKeyEvent(KeyEvent keyEvent) {
        this.f$0.lambda$onSendLongClick$12(keyEvent);
    }
}
