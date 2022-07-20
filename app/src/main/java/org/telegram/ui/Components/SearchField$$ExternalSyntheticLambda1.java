package org.telegram.ui.Components;

import android.view.KeyEvent;
import android.widget.TextView;
/* loaded from: classes3.dex */
public final /* synthetic */ class SearchField$$ExternalSyntheticLambda1 implements TextView.OnEditorActionListener {
    public final /* synthetic */ SearchField f$0;

    public /* synthetic */ SearchField$$ExternalSyntheticLambda1(SearchField searchField) {
        this.f$0 = searchField;
    }

    @Override // android.widget.TextView.OnEditorActionListener
    public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        boolean lambda$new$1;
        lambda$new$1 = this.f$0.lambda$new$1(textView, i, keyEvent);
        return lambda$new$1;
    }
}
