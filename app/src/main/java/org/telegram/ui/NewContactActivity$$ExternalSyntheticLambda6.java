package org.telegram.ui;

import android.view.KeyEvent;
import android.widget.TextView;
/* loaded from: classes3.dex */
public final /* synthetic */ class NewContactActivity$$ExternalSyntheticLambda6 implements TextView.OnEditorActionListener {
    public final /* synthetic */ NewContactActivity f$0;

    public /* synthetic */ NewContactActivity$$ExternalSyntheticLambda6(NewContactActivity newContactActivity) {
        this.f$0 = newContactActivity;
    }

    @Override // android.widget.TextView.OnEditorActionListener
    public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        boolean lambda$createView$2;
        lambda$createView$2 = this.f$0.lambda$createView$2(textView, i, keyEvent);
        return lambda$createView$2;
    }
}
