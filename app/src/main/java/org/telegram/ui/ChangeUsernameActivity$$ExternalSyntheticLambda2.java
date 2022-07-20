package org.telegram.ui;

import android.view.KeyEvent;
import android.widget.TextView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChangeUsernameActivity$$ExternalSyntheticLambda2 implements TextView.OnEditorActionListener {
    public final /* synthetic */ ChangeUsernameActivity f$0;

    public /* synthetic */ ChangeUsernameActivity$$ExternalSyntheticLambda2(ChangeUsernameActivity changeUsernameActivity) {
        this.f$0 = changeUsernameActivity;
    }

    @Override // android.widget.TextView.OnEditorActionListener
    public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        boolean lambda$createView$1;
        lambda$createView$1 = this.f$0.lambda$createView$1(textView, i, keyEvent);
        return lambda$createView$1;
    }
}
