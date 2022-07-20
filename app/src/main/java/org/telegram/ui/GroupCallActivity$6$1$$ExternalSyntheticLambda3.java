package org.telegram.ui;

import android.content.DialogInterface;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.GroupCallActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class GroupCallActivity$6$1$$ExternalSyntheticLambda3 implements DialogInterface.OnShowListener {
    public final /* synthetic */ GroupCallActivity.AnonymousClass6.AnonymousClass1 f$0;
    public final /* synthetic */ AlertDialog f$1;
    public final /* synthetic */ EditTextBoldCursor f$2;

    public /* synthetic */ GroupCallActivity$6$1$$ExternalSyntheticLambda3(GroupCallActivity.AnonymousClass6.AnonymousClass1 anonymousClass1, AlertDialog alertDialog, EditTextBoldCursor editTextBoldCursor) {
        this.f$0 = anonymousClass1;
        this.f$1 = alertDialog;
        this.f$2 = editTextBoldCursor;
    }

    @Override // android.content.DialogInterface.OnShowListener
    public final void onShow(DialogInterface dialogInterface) {
        this.f$0.lambda$onStartRecord$1(this.f$1, this.f$2, dialogInterface);
    }
}
