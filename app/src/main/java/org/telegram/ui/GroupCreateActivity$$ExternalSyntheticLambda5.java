package org.telegram.ui;

import android.view.KeyEvent;
import android.widget.TextView;
/* loaded from: classes3.dex */
public final /* synthetic */ class GroupCreateActivity$$ExternalSyntheticLambda5 implements TextView.OnEditorActionListener {
    public final /* synthetic */ GroupCreateActivity f$0;

    public /* synthetic */ GroupCreateActivity$$ExternalSyntheticLambda5(GroupCreateActivity groupCreateActivity) {
        this.f$0 = groupCreateActivity;
    }

    @Override // android.widget.TextView.OnEditorActionListener
    public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        boolean lambda$createView$1;
        lambda$createView$1 = this.f$0.lambda$createView$1(textView, i, keyEvent);
        return lambda$createView$1;
    }
}
