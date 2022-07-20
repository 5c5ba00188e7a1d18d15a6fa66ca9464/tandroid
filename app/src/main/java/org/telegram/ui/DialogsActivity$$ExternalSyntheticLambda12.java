package org.telegram.ui;

import android.content.DialogInterface;
/* loaded from: classes3.dex */
public final /* synthetic */ class DialogsActivity$$ExternalSyntheticLambda12 implements DialogInterface.OnClickListener {
    public final /* synthetic */ DialogsActivity f$0;
    public final /* synthetic */ long f$1;

    public /* synthetic */ DialogsActivity$$ExternalSyntheticLambda12(DialogsActivity dialogsActivity, long j) {
        this.f$0 = dialogsActivity;
        this.f$1 = j;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$didSelectResult$55(this.f$1, dialogInterface, i);
    }
}
