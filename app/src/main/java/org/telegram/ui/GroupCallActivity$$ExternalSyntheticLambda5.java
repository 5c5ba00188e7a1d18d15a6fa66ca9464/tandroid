package org.telegram.ui;

import android.content.DialogInterface;
/* loaded from: classes3.dex */
public final /* synthetic */ class GroupCallActivity$$ExternalSyntheticLambda5 implements DialogInterface.OnCancelListener {
    public final /* synthetic */ GroupCallActivity f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ GroupCallActivity$$ExternalSyntheticLambda5(GroupCallActivity groupCallActivity, int i) {
        this.f$0 = groupCallActivity;
        this.f$1 = i;
    }

    @Override // android.content.DialogInterface.OnCancelListener
    public final void onCancel(DialogInterface dialogInterface) {
        this.f$0.lambda$inviteUserToCall$47(this.f$1, dialogInterface);
    }
}
