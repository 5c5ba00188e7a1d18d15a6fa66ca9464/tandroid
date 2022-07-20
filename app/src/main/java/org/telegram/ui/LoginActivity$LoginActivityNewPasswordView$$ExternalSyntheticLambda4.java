package org.telegram.ui;

import android.view.KeyEvent;
import android.widget.TextView;
import org.telegram.ui.LoginActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class LoginActivity$LoginActivityNewPasswordView$$ExternalSyntheticLambda4 implements TextView.OnEditorActionListener {
    public final /* synthetic */ LoginActivity.LoginActivityNewPasswordView f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ LoginActivity$LoginActivityNewPasswordView$$ExternalSyntheticLambda4(LoginActivity.LoginActivityNewPasswordView loginActivityNewPasswordView, int i) {
        this.f$0 = loginActivityNewPasswordView;
        this.f$1 = i;
    }

    @Override // android.widget.TextView.OnEditorActionListener
    public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        boolean lambda$new$2;
        lambda$new$2 = this.f$0.lambda$new$2(this.f$1, textView, i, keyEvent);
        return lambda$new$2;
    }
}
