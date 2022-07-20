package org.telegram.ui;

import org.telegram.tgnet.TLObject;
/* loaded from: classes3.dex */
public final /* synthetic */ class TwoStepVerificationActivity$$ExternalSyntheticLambda16 implements Runnable {
    public final /* synthetic */ TwoStepVerificationActivity f$0;
    public final /* synthetic */ TLObject f$1;

    public /* synthetic */ TwoStepVerificationActivity$$ExternalSyntheticLambda16(TwoStepVerificationActivity twoStepVerificationActivity, TLObject tLObject) {
        this.f$0 = twoStepVerificationActivity;
        this.f$1 = tLObject;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$cancelPasswordReset$8(this.f$1);
    }
}
