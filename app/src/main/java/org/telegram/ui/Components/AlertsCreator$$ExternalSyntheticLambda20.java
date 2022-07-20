package org.telegram.ui.Components;

import android.content.DialogInterface;
import org.telegram.messenger.MessagesStorage;
/* loaded from: classes3.dex */
public final /* synthetic */ class AlertsCreator$$ExternalSyntheticLambda20 implements DialogInterface.OnClickListener {
    public final /* synthetic */ MessagesStorage.IntCallback f$0;

    public /* synthetic */ AlertsCreator$$ExternalSyntheticLambda20(MessagesStorage.IntCallback intCallback) {
        this.f$0 = intCallback;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.run(0);
    }
}
