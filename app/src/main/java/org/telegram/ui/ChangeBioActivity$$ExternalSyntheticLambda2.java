package org.telegram.ui;

import android.view.KeyEvent;
import android.widget.TextView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChangeBioActivity$$ExternalSyntheticLambda2 implements TextView.OnEditorActionListener {
    public final /* synthetic */ ChangeBioActivity f$0;

    public /* synthetic */ ChangeBioActivity$$ExternalSyntheticLambda2(ChangeBioActivity changeBioActivity) {
        this.f$0 = changeBioActivity;
    }

    @Override // android.widget.TextView.OnEditorActionListener
    public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        boolean lambda$createView$1;
        lambda$createView$1 = this.f$0.lambda$createView$1(textView, i, keyEvent);
        return lambda$createView$1;
    }
}
