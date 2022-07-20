package org.telegram.ui;

import android.content.DialogInterface;
import java.util.HashMap;
/* loaded from: classes3.dex */
public final /* synthetic */ class LaunchActivity$$ExternalSyntheticLambda12 implements DialogInterface.OnClickListener {
    public final /* synthetic */ LaunchActivity f$0;
    public final /* synthetic */ HashMap f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ LaunchActivity$$ExternalSyntheticLambda12(LaunchActivity launchActivity, HashMap hashMap, int i) {
        this.f$0 = launchActivity;
        this.f$1 = hashMap;
        this.f$2 = i;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$didReceivedNotification$84(this.f$1, this.f$2, dialogInterface, i);
    }
}
