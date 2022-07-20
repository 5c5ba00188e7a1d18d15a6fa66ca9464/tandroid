package org.telegram.ui.Components;

import android.content.DialogInterface;
import org.telegram.ui.LaunchActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class AlertsCreator$$ExternalSyntheticLambda31 implements DialogInterface.OnClickListener {
    public final /* synthetic */ LaunchActivity f$0;

    public /* synthetic */ AlertsCreator$$ExternalSyntheticLambda31(LaunchActivity launchActivity) {
        this.f$0 = launchActivity;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        AlertsCreator.lambda$createLanguageAlert$7(this.f$0, dialogInterface, i);
    }
}
