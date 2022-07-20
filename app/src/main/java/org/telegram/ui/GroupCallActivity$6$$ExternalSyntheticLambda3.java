package org.telegram.ui;

import android.content.DialogInterface;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.GroupCallActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class GroupCallActivity$6$$ExternalSyntheticLambda3 implements DialogInterface.OnClickListener {
    public final /* synthetic */ GroupCallActivity.AnonymousClass6 f$0;
    public final /* synthetic */ EditTextBoldCursor f$1;
    public final /* synthetic */ AlertDialog.Builder f$2;

    public /* synthetic */ GroupCallActivity$6$$ExternalSyntheticLambda3(GroupCallActivity.AnonymousClass6 anonymousClass6, EditTextBoldCursor editTextBoldCursor, AlertDialog.Builder builder) {
        this.f$0 = anonymousClass6;
        this.f$1 = editTextBoldCursor;
        this.f$2 = builder;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$onItemClick$5(this.f$1, this.f$2, dialogInterface, i);
    }
}
