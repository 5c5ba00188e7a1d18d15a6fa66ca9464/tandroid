package org.telegram.ui.Components;

import android.view.MotionEvent;
import android.view.View;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
/* loaded from: classes3.dex */
public final /* synthetic */ class AlertsCreator$$ExternalSyntheticLambda75 implements View.OnTouchListener {
    public final /* synthetic */ ActionBarPopupWindow f$0;
    public final /* synthetic */ android.graphics.Rect f$1;

    public /* synthetic */ AlertsCreator$$ExternalSyntheticLambda75(ActionBarPopupWindow actionBarPopupWindow, android.graphics.Rect rect) {
        this.f$0 = actionBarPopupWindow;
        this.f$1 = rect;
    }

    @Override // android.view.View.OnTouchListener
    public final boolean onTouch(View view, MotionEvent motionEvent) {
        boolean lambda$showPopupMenu$128;
        lambda$showPopupMenu$128 = AlertsCreator.lambda$showPopupMenu$128(this.f$0, this.f$1, view, motionEvent);
        return lambda$showPopupMenu$128;
    }
}
