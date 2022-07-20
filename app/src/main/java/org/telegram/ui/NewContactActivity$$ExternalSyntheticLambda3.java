package org.telegram.ui;

import android.view.KeyEvent;
import android.widget.TextView;
/* loaded from: classes3.dex */
public final /* synthetic */ class NewContactActivity$$ExternalSyntheticLambda3 implements TextView.OnEditorActionListener {
    public final /* synthetic */ NewContactActivity f$0;

    public /* synthetic */ NewContactActivity$$ExternalSyntheticLambda3(NewContactActivity newContactActivity) {
        this.f$0 = newContactActivity;
    }

    @Override // android.widget.TextView.OnEditorActionListener
    public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        boolean lambda$createView$5;
        lambda$createView$5 = this.f$0.lambda$createView$5(textView, i, keyEvent);
        return lambda$createView$5;
    }
}
