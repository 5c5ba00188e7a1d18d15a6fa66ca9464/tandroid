package org.telegram.ui.Components;

import android.content.DialogInterface;
import org.telegram.messenger.AccountInstance;
/* loaded from: classes3.dex */
public final /* synthetic */ class JoinCallAlert$$ExternalSyntheticLambda1 implements DialogInterface.OnCancelListener {
    public final /* synthetic */ AccountInstance f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ JoinCallAlert$$ExternalSyntheticLambda1(AccountInstance accountInstance, int i) {
        this.f$0 = accountInstance;
        this.f$1 = i;
    }

    @Override // android.content.DialogInterface.OnCancelListener
    public final void onCancel(DialogInterface dialogInterface) {
        JoinCallAlert.lambda$checkFewUsers$2(this.f$0, this.f$1, dialogInterface);
    }
}
