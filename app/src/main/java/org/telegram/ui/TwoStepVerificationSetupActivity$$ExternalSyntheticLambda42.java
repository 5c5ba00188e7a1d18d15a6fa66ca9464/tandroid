package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class TwoStepVerificationSetupActivity$$ExternalSyntheticLambda42 implements RequestDelegate {
    public final /* synthetic */ TwoStepVerificationSetupActivity f$0;

    public /* synthetic */ TwoStepVerificationSetupActivity$$ExternalSyntheticLambda42(TwoStepVerificationSetupActivity twoStepVerificationSetupActivity) {
        this.f$0 = twoStepVerificationSetupActivity;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$processNext$25(tLObject, tLRPC$TL_error);
    }
}
