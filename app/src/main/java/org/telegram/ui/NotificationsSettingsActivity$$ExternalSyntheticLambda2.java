package org.telegram.ui;

import android.content.DialogInterface;
import java.util.ArrayList;
/* loaded from: classes3.dex */
public final /* synthetic */ class NotificationsSettingsActivity$$ExternalSyntheticLambda2 implements DialogInterface.OnClickListener {
    public final /* synthetic */ NotificationsSettingsActivity f$0;
    public final /* synthetic */ ArrayList f$1;

    public /* synthetic */ NotificationsSettingsActivity$$ExternalSyntheticLambda2(NotificationsSettingsActivity notificationsSettingsActivity, ArrayList arrayList) {
        this.f$0 = notificationsSettingsActivity;
        this.f$1 = arrayList;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$showExceptionsAlert$9(this.f$1, dialogInterface, i);
    }
}
