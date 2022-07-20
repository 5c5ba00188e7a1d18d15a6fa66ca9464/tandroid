package org.telegram.ui.Components;

import android.content.DialogInterface;
import org.telegram.ui.Components.AlertsCreator;
/* loaded from: classes3.dex */
public final /* synthetic */ class AlertsCreator$$ExternalSyntheticLambda29 implements DialogInterface.OnClickListener {
    public final /* synthetic */ AlertsCreator.BlockDialogCallback f$0;
    public final /* synthetic */ boolean[] f$1;

    public /* synthetic */ AlertsCreator$$ExternalSyntheticLambda29(AlertsCreator.BlockDialogCallback blockDialogCallback, boolean[] zArr) {
        this.f$0 = blockDialogCallback;
        this.f$1 = zArr;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        AlertsCreator.lambda$createBlockDialogAlert$39(this.f$0, this.f$1, dialogInterface, i);
    }
}
