package org.telegram.ui.ActionBar;

import android.content.DialogInterface;
/* loaded from: classes3.dex */
public final /* synthetic */ class AlertDialog$$ExternalSyntheticLambda0 implements DialogInterface.OnClickListener {
    public final /* synthetic */ AlertDialog f$0;

    public /* synthetic */ AlertDialog$$ExternalSyntheticLambda0(AlertDialog alertDialog) {
        this.f$0 = alertDialog;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$showCancelAlert$5(dialogInterface, i);
    }
}
