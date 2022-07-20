package org.telegram.ui.Components;

import android.view.ViewTreeObserver;
/* loaded from: classes3.dex */
public final /* synthetic */ class EditTextBoldCursor$$ExternalSyntheticLambda0 implements ViewTreeObserver.OnPreDrawListener {
    public final /* synthetic */ EditTextBoldCursor f$0;

    public /* synthetic */ EditTextBoldCursor$$ExternalSyntheticLambda0(EditTextBoldCursor editTextBoldCursor) {
        this.f$0 = editTextBoldCursor;
    }

    @Override // android.view.ViewTreeObserver.OnPreDrawListener
    public final boolean onPreDraw() {
        boolean lambda$startActionMode$0;
        lambda$startActionMode$0 = this.f$0.lambda$startActionMode$0();
        return lambda$startActionMode$0;
    }
}
