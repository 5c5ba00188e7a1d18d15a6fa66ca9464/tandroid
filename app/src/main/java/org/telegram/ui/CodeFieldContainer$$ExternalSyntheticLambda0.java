package org.telegram.ui;

import android.view.KeyEvent;
import android.widget.TextView;
/* loaded from: classes3.dex */
public final /* synthetic */ class CodeFieldContainer$$ExternalSyntheticLambda0 implements TextView.OnEditorActionListener {
    public final /* synthetic */ CodeFieldContainer f$0;

    public /* synthetic */ CodeFieldContainer$$ExternalSyntheticLambda0(CodeFieldContainer codeFieldContainer) {
        this.f$0 = codeFieldContainer;
    }

    @Override // android.widget.TextView.OnEditorActionListener
    public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        boolean lambda$setNumbersCount$0;
        lambda$setNumbersCount$0 = this.f$0.lambda$setNumbersCount$0(textView, i, keyEvent);
        return lambda$setNumbersCount$0;
    }
}
