package org.telegram.ui;

import android.content.DialogInterface;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.GroupCallActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class GroupCallActivity$6$1$$ExternalSyntheticLambda1 implements DialogInterface.OnClickListener {
    public final /* synthetic */ GroupCallActivity.AnonymousClass6.AnonymousClass1 f$0;
    public final /* synthetic */ EditTextBoldCursor f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ GroupCallActivity$6$1$$ExternalSyntheticLambda1(GroupCallActivity.AnonymousClass6.AnonymousClass1 anonymousClass1, EditTextBoldCursor editTextBoldCursor, int i) {
        this.f$0 = anonymousClass1;
        this.f$1 = editTextBoldCursor;
        this.f$2 = i;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$onStartRecord$3(this.f$1, this.f$2, dialogInterface, i);
    }
}
