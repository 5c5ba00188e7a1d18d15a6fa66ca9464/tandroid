package org.telegram.ui.Components;

import android.content.Context;
import android.content.DialogInterface;
/* loaded from: classes3.dex */
public final /* synthetic */ class AlertsCreator$$ExternalSyntheticLambda12 implements DialogInterface.OnClickListener {
    public final /* synthetic */ Context f$0;

    public /* synthetic */ AlertsCreator$$ExternalSyntheticLambda12(Context context) {
        this.f$0 = context;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        AlertsCreator.lambda$showUpdateAppAlert$6(this.f$0, dialogInterface, i);
    }
}
