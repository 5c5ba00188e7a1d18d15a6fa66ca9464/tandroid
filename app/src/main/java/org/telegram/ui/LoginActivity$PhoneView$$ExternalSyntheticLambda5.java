package org.telegram.ui;

import android.view.KeyEvent;
import android.widget.TextView;
import org.telegram.ui.LoginActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class LoginActivity$PhoneView$$ExternalSyntheticLambda5 implements TextView.OnEditorActionListener {
    public final /* synthetic */ LoginActivity.PhoneView f$0;

    public /* synthetic */ LoginActivity$PhoneView$$ExternalSyntheticLambda5(LoginActivity.PhoneView phoneView) {
        this.f$0 = phoneView;
    }

    @Override // android.widget.TextView.OnEditorActionListener
    public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        boolean lambda$new$5;
        lambda$new$5 = this.f$0.lambda$new$5(textView, i, keyEvent);
        return lambda$new$5;
    }
}
