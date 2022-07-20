package org.telegram.ui;

import android.content.DialogInterface;
/* loaded from: classes3.dex */
public final /* synthetic */ class PopupNotificationActivity$$ExternalSyntheticLambda0 implements DialogInterface.OnClickListener {
    public final /* synthetic */ PopupNotificationActivity f$0;

    public /* synthetic */ PopupNotificationActivity$$ExternalSyntheticLambda0(PopupNotificationActivity popupNotificationActivity) {
        this.f$0 = popupNotificationActivity;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$onRequestPermissionsResult$0(dialogInterface, i);
    }
}
