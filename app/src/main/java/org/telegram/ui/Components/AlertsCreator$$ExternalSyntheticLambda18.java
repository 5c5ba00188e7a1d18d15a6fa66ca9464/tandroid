package org.telegram.ui.Components;

import android.content.DialogInterface;
import org.telegram.messenger.MessagesStorage;
/* loaded from: classes3.dex */
public final /* synthetic */ class AlertsCreator$$ExternalSyntheticLambda18 implements DialogInterface.OnClickListener {
    public final /* synthetic */ MessagesStorage.BooleanCallback f$0;
    public final /* synthetic */ boolean[] f$1;

    public /* synthetic */ AlertsCreator$$ExternalSyntheticLambda18(MessagesStorage.BooleanCallback booleanCallback, boolean[] zArr) {
        this.f$0 = booleanCallback;
        this.f$1 = zArr;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        AlertsCreator.lambda$createClearDaysDialogAlert$27(this.f$0, this.f$1, dialogInterface, i);
    }
}
