package org.telegram.ui.Components;

import android.view.View;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.Components.AlertsCreator;
/* loaded from: classes3.dex */
public final /* synthetic */ class AlertsCreator$$ExternalSyntheticLambda64 implements View.OnClickListener {
    public final /* synthetic */ AlertDialog[] f$0;
    public final /* synthetic */ Runnable f$1;
    public final /* synthetic */ AlertsCreator.AccountSelectDelegate f$2;

    public /* synthetic */ AlertsCreator$$ExternalSyntheticLambda64(AlertDialog[] alertDialogArr, Runnable runnable, AlertsCreator.AccountSelectDelegate accountSelectDelegate) {
        this.f$0 = alertDialogArr;
        this.f$1 = runnable;
        this.f$2 = accountSelectDelegate;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        AlertsCreator.lambda$createAccountSelectDialog$109(this.f$0, this.f$1, this.f$2, view);
    }
}
