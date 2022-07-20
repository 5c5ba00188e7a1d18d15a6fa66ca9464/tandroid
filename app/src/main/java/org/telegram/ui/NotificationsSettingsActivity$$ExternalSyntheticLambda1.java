package org.telegram.ui;

import android.content.DialogInterface;
/* loaded from: classes3.dex */
public final /* synthetic */ class NotificationsSettingsActivity$$ExternalSyntheticLambda1 implements DialogInterface.OnClickListener {
    public final /* synthetic */ NotificationsSettingsActivity f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ NotificationsSettingsActivity$$ExternalSyntheticLambda1(NotificationsSettingsActivity notificationsSettingsActivity, int i) {
        this.f$0 = notificationsSettingsActivity;
        this.f$1 = i;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$createView$7(this.f$1, dialogInterface, i);
    }
}
