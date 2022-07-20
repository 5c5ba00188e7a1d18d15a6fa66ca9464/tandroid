package org.telegram.ui;

import org.telegram.tgnet.TLRPC$InputCheckPasswordSRP;
import org.telegram.ui.TwoStepVerificationActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatRightsEditActivity$$ExternalSyntheticLambda26 implements TwoStepVerificationActivity.TwoStepVerificationActivityDelegate {
    public final /* synthetic */ ChatRightsEditActivity f$0;
    public final /* synthetic */ TwoStepVerificationActivity f$1;

    public /* synthetic */ ChatRightsEditActivity$$ExternalSyntheticLambda26(ChatRightsEditActivity chatRightsEditActivity, TwoStepVerificationActivity twoStepVerificationActivity) {
        this.f$0 = chatRightsEditActivity;
        this.f$1 = twoStepVerificationActivity;
    }

    @Override // org.telegram.ui.TwoStepVerificationActivity.TwoStepVerificationActivityDelegate
    public final void didEnterPassword(TLRPC$InputCheckPasswordSRP tLRPC$InputCheckPasswordSRP) {
        this.f$0.lambda$initTransfer$8(this.f$1, tLRPC$InputCheckPasswordSRP);
    }
}
