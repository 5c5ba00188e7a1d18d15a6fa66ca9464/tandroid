package org.telegram.ui.Components;

import android.content.DialogInterface;
import android.view.KeyEvent;
import android.widget.TextView;
import org.telegram.ui.ActionBar.AlertDialog;
/* loaded from: classes3.dex */
public final /* synthetic */ class AlertsCreator$$ExternalSyntheticLambda83 implements TextView.OnEditorActionListener {
    public final /* synthetic */ AlertDialog f$0;
    public final /* synthetic */ DialogInterface.OnClickListener f$1;

    public /* synthetic */ AlertsCreator$$ExternalSyntheticLambda83(AlertDialog alertDialog, DialogInterface.OnClickListener onClickListener) {
        this.f$0 = alertDialog;
        this.f$1 = onClickListener;
    }

    @Override // android.widget.TextView.OnEditorActionListener
    public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        boolean lambda$createChangeNameAlert$36;
        lambda$createChangeNameAlert$36 = AlertsCreator.lambda$createChangeNameAlert$36(this.f$0, this.f$1, textView, i, keyEvent);
        return lambda$createChangeNameAlert$36;
    }
}
