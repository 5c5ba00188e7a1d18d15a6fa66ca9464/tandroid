package org.telegram.ui.Components;

import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
/* loaded from: classes3.dex */
public final /* synthetic */ class AlertsCreator$$ExternalSyntheticLambda88 implements Runnable {
    public final /* synthetic */ EditTextBoldCursor f$0;
    public final /* synthetic */ AlertDialog f$1;
    public final /* synthetic */ BaseFragment f$2;

    public /* synthetic */ AlertsCreator$$ExternalSyntheticLambda88(EditTextBoldCursor editTextBoldCursor, AlertDialog alertDialog, BaseFragment baseFragment) {
        this.f$0 = editTextBoldCursor;
        this.f$1 = alertDialog;
        this.f$2 = baseFragment;
    }

    @Override // java.lang.Runnable
    public final void run() {
        AlertsCreator.processCreate(this.f$0, this.f$1, this.f$2);
    }
}
