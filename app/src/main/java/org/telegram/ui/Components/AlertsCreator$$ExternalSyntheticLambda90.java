package org.telegram.ui.Components;

import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
/* loaded from: classes3.dex */
public final /* synthetic */ class AlertsCreator$$ExternalSyntheticLambda90 implements Runnable {
    public final /* synthetic */ AlertDialog[] f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ BaseFragment f$3;

    public /* synthetic */ AlertsCreator$$ExternalSyntheticLambda90(AlertDialog[] alertDialogArr, int i, int i2, BaseFragment baseFragment) {
        this.f$0 = alertDialogArr;
        this.f$1 = i;
        this.f$2 = i2;
        this.f$3 = baseFragment;
    }

    @Override // java.lang.Runnable
    public final void run() {
        AlertsCreator.lambda$createDeleteMessagesAlert$113(this.f$0, this.f$1, this.f$2, this.f$3);
    }
}
