package org.telegram.ui.Components;

import android.content.DialogInterface;
/* loaded from: classes3.dex */
public final /* synthetic */ class PasscodeView$$ExternalSyntheticLambda0 implements DialogInterface.OnDismissListener {
    public final /* synthetic */ PasscodeView f$0;

    public /* synthetic */ PasscodeView$$ExternalSyntheticLambda0(PasscodeView passcodeView) {
        this.f$0 = passcodeView;
    }

    @Override // android.content.DialogInterface.OnDismissListener
    public final void onDismiss(DialogInterface dialogInterface) {
        this.f$0.lambda$checkFingerprint$7(dialogInterface);
    }
}
