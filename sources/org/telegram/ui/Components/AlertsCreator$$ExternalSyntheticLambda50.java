package org.telegram.ui.Components;

import android.content.DialogInterface;
import org.telegram.messenger.SharedConfig;
/* loaded from: classes4.dex */
public final /* synthetic */ class AlertsCreator$$ExternalSyntheticLambda50 implements DialogInterface.OnDismissListener {
    public static final /* synthetic */ AlertsCreator$$ExternalSyntheticLambda50 INSTANCE = new AlertsCreator$$ExternalSyntheticLambda50();

    private /* synthetic */ AlertsCreator$$ExternalSyntheticLambda50() {
    }

    @Override // android.content.DialogInterface.OnDismissListener
    public final void onDismiss(DialogInterface dialogInterface) {
        SharedConfig.BackgroundActivityPrefs.increaseDismissedCount();
    }
}
