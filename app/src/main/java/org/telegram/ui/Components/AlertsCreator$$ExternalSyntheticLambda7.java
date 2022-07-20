package org.telegram.ui.Components;

import android.app.Activity;
import android.content.DialogInterface;
/* loaded from: classes3.dex */
public final /* synthetic */ class AlertsCreator$$ExternalSyntheticLambda7 implements DialogInterface.OnClickListener {
    public final /* synthetic */ Activity f$0;

    public /* synthetic */ AlertsCreator$$ExternalSyntheticLambda7(Activity activity) {
        this.f$0 = activity;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        AlertsCreator.lambda$createBackgroundLocationPermissionDialog$95(this.f$0, dialogInterface, i);
    }
}
