package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class TwoStepVerificationActivity$$ExternalSyntheticLambda29 implements RequestDelegate {
    public final /* synthetic */ TwoStepVerificationActivity f$0;

    public /* synthetic */ TwoStepVerificationActivity$$ExternalSyntheticLambda29(TwoStepVerificationActivity twoStepVerificationActivity) {
        this.f$0 = twoStepVerificationActivity;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$resetPassword$13(tLObject, tLRPC$TL_error);
    }
}
