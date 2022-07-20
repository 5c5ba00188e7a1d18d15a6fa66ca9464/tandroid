package org.telegram.ui;

import android.view.KeyEvent;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
/* loaded from: classes3.dex */
public final /* synthetic */ class PhotoPickerActivity$$ExternalSyntheticLambda9 implements ActionBarPopupWindow.OnDispatchKeyEventListener {
    public final /* synthetic */ PhotoPickerActivity f$0;

    public /* synthetic */ PhotoPickerActivity$$ExternalSyntheticLambda9(PhotoPickerActivity photoPickerActivity) {
        this.f$0 = photoPickerActivity;
    }

    @Override // org.telegram.ui.ActionBar.ActionBarPopupWindow.OnDispatchKeyEventListener
    public final void onDispatchKeyEvent(KeyEvent keyEvent) {
        this.f$0.lambda$createView$5(keyEvent);
    }
}
