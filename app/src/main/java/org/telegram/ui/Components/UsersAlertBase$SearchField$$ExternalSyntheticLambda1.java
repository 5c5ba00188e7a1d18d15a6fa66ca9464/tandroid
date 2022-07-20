package org.telegram.ui.Components;

import android.view.KeyEvent;
import android.widget.TextView;
import org.telegram.ui.Components.UsersAlertBase;
/* loaded from: classes3.dex */
public final /* synthetic */ class UsersAlertBase$SearchField$$ExternalSyntheticLambda1 implements TextView.OnEditorActionListener {
    public final /* synthetic */ UsersAlertBase.SearchField f$0;

    public /* synthetic */ UsersAlertBase$SearchField$$ExternalSyntheticLambda1(UsersAlertBase.SearchField searchField) {
        this.f$0 = searchField;
    }

    @Override // android.widget.TextView.OnEditorActionListener
    public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        boolean lambda$new$1;
        lambda$new$1 = this.f$0.lambda$new$1(textView, i, keyEvent);
        return lambda$new$1;
    }
}
