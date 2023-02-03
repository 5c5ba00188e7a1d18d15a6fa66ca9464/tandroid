package org.telegram.ui.Components;

import android.content.DialogInterface;
import org.telegram.messenger.SharedConfig;
/* loaded from: classes3.dex */
public final /* synthetic */ class AlertsCreator$$ExternalSyntheticLambda48 implements DialogInterface.OnDismissListener {
    public static final /* synthetic */ AlertsCreator$$ExternalSyntheticLambda48 INSTANCE = new AlertsCreator$$ExternalSyntheticLambda48();

    private /* synthetic */ AlertsCreator$$ExternalSyntheticLambda48() {
    }

    @Override // android.content.DialogInterface.OnDismissListener
    public final void onDismiss(DialogInterface dialogInterface) {
        SharedConfig.BackgroundActivityPrefs.increaseDismissedCount();
    }
}
