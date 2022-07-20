package org.telegram.ui.ActionBar;

import android.view.KeyEvent;
import android.widget.TextView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ActionBarMenuItem$$ExternalSyntheticLambda10 implements TextView.OnEditorActionListener {
    public final /* synthetic */ ActionBarMenuItem f$0;

    public /* synthetic */ ActionBarMenuItem$$ExternalSyntheticLambda10(ActionBarMenuItem actionBarMenuItem) {
        this.f$0 = actionBarMenuItem;
    }

    @Override // android.widget.TextView.OnEditorActionListener
    public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        boolean lambda$setIsSearchField$12;
        lambda$setIsSearchField$12 = this.f$0.lambda$setIsSearchField$12(textView, i, keyEvent);
        return lambda$setIsSearchField$12;
    }
}
