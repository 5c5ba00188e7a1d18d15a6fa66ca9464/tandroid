package org.telegram.ui;

import android.view.KeyEvent;
import android.widget.TextView;
/* loaded from: classes3.dex */
public final /* synthetic */ class PassportActivity$$ExternalSyntheticLambda39 implements TextView.OnEditorActionListener {
    public final /* synthetic */ PassportActivity f$0;

    public /* synthetic */ PassportActivity$$ExternalSyntheticLambda39(PassportActivity passportActivity) {
        this.f$0 = passportActivity;
    }

    @Override // android.widget.TextView.OnEditorActionListener
    public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        boolean lambda$createEmailVerificationInterface$5;
        lambda$createEmailVerificationInterface$5 = this.f$0.lambda$createEmailVerificationInterface$5(textView, i, keyEvent);
        return lambda$createEmailVerificationInterface$5;
    }
}
