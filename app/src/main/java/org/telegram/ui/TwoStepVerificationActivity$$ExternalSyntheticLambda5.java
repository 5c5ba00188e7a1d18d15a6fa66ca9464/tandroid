package org.telegram.ui;

import android.content.DialogInterface;
/* loaded from: classes3.dex */
public final /* synthetic */ class TwoStepVerificationActivity$$ExternalSyntheticLambda5 implements DialogInterface.OnDismissListener {
    public final /* synthetic */ TwoStepVerificationActivity f$0;

    public /* synthetic */ TwoStepVerificationActivity$$ExternalSyntheticLambda5(TwoStepVerificationActivity twoStepVerificationActivity) {
        this.f$0 = twoStepVerificationActivity;
    }

    @Override // android.content.DialogInterface.OnDismissListener
    public final void onDismiss(DialogInterface dialogInterface) {
        this.f$0.lambda$resetPassword$11(dialogInterface);
    }
}
