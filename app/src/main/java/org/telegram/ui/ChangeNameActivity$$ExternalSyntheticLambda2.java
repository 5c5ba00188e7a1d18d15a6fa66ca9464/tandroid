package org.telegram.ui;

import android.view.KeyEvent;
import android.widget.TextView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChangeNameActivity$$ExternalSyntheticLambda2 implements TextView.OnEditorActionListener {
    public final /* synthetic */ ChangeNameActivity f$0;

    public /* synthetic */ ChangeNameActivity$$ExternalSyntheticLambda2(ChangeNameActivity changeNameActivity) {
        this.f$0 = changeNameActivity;
    }

    @Override // android.widget.TextView.OnEditorActionListener
    public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        boolean lambda$createView$2;
        lambda$createView$2 = this.f$0.lambda$createView$2(textView, i, keyEvent);
        return lambda$createView$2;
    }
}
