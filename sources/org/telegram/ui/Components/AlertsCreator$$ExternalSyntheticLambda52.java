package org.telegram.ui.Components;

import android.content.DialogInterface;
import org.telegram.messenger.SharedConfig;
/* loaded from: classes4.dex */
public final /* synthetic */ class AlertsCreator$$ExternalSyntheticLambda52 implements DialogInterface.OnDismissListener {
    public static final /* synthetic */ AlertsCreator$$ExternalSyntheticLambda52 INSTANCE = new AlertsCreator$$ExternalSyntheticLambda52();

    private /* synthetic */ AlertsCreator$$ExternalSyntheticLambda52() {
    }

    @Override // android.content.DialogInterface.OnDismissListener
    public final void onDismiss(DialogInterface dialogInterface) {
        SharedConfig.BackgroundActivityPrefs.increaseDismissedCount();
    }
}
