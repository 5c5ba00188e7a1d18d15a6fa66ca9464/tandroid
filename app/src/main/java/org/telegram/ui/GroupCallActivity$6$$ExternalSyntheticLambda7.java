package org.telegram.ui;

import android.view.KeyEvent;
import android.widget.TextView;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.GroupCallActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class GroupCallActivity$6$$ExternalSyntheticLambda7 implements TextView.OnEditorActionListener {
    public final /* synthetic */ AlertDialog.Builder f$0;

    public /* synthetic */ GroupCallActivity$6$$ExternalSyntheticLambda7(AlertDialog.Builder builder) {
        this.f$0 = builder;
    }

    @Override // android.widget.TextView.OnEditorActionListener
    public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        boolean lambda$onItemClick$4;
        lambda$onItemClick$4 = GroupCallActivity.AnonymousClass6.lambda$onItemClick$4(this.f$0, textView, i, keyEvent);
        return lambda$onItemClick$4;
    }
}
