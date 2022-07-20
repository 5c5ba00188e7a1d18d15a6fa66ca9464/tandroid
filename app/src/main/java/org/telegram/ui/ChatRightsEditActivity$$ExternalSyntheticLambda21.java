package org.telegram.ui;

import org.telegram.messenger.MessagesStorage;
import org.telegram.tgnet.TLRPC$InputCheckPasswordSRP;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatRightsEditActivity$$ExternalSyntheticLambda21 implements MessagesStorage.LongCallback {
    public final /* synthetic */ ChatRightsEditActivity f$0;
    public final /* synthetic */ TLRPC$InputCheckPasswordSRP f$1;
    public final /* synthetic */ TwoStepVerificationActivity f$2;

    public /* synthetic */ ChatRightsEditActivity$$ExternalSyntheticLambda21(ChatRightsEditActivity chatRightsEditActivity, TLRPC$InputCheckPasswordSRP tLRPC$InputCheckPasswordSRP, TwoStepVerificationActivity twoStepVerificationActivity) {
        this.f$0 = chatRightsEditActivity;
        this.f$1 = tLRPC$InputCheckPasswordSRP;
        this.f$2 = twoStepVerificationActivity;
    }

    @Override // org.telegram.messenger.MessagesStorage.LongCallback
    public final void run(long j) {
        this.f$0.lambda$initTransfer$7(this.f$1, this.f$2, j);
    }
}
