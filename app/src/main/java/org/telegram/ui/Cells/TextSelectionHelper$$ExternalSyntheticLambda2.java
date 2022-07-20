package org.telegram.ui.Cells;

import android.view.MotionEvent;
import android.view.View;
/* loaded from: classes3.dex */
public final /* synthetic */ class TextSelectionHelper$$ExternalSyntheticLambda2 implements View.OnTouchListener {
    public final /* synthetic */ TextSelectionHelper f$0;

    public /* synthetic */ TextSelectionHelper$$ExternalSyntheticLambda2(TextSelectionHelper textSelectionHelper) {
        this.f$0 = textSelectionHelper;
    }

    @Override // android.view.View.OnTouchListener
    public final boolean onTouch(View view, MotionEvent motionEvent) {
        boolean lambda$showActions$1;
        lambda$showActions$1 = this.f$0.lambda$showActions$1(view, motionEvent);
        return lambda$showActions$1;
    }
}
