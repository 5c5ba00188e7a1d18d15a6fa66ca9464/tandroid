package org.telegram.ui;

import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class TwoStepVerificationSetupActivity$$ExternalSyntheticLambda34 implements Runnable {
    public final /* synthetic */ TwoStepVerificationSetupActivity f$0;
    public final /* synthetic */ TLRPC$TL_error f$1;

    public /* synthetic */ TwoStepVerificationSetupActivity$$ExternalSyntheticLambda34(TwoStepVerificationSetupActivity twoStepVerificationSetupActivity, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0 = twoStepVerificationSetupActivity;
        this.f$1 = tLRPC$TL_error;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$setNewPassword$43(this.f$1);
    }
}
