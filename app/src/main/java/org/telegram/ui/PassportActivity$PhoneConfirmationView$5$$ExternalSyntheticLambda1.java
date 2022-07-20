package org.telegram.ui;

import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.ui.PassportActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class PassportActivity$PhoneConfirmationView$5$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ PassportActivity.PhoneConfirmationView.AnonymousClass5 f$0;
    public final /* synthetic */ TLRPC$TL_error f$1;

    public /* synthetic */ PassportActivity$PhoneConfirmationView$5$$ExternalSyntheticLambda1(PassportActivity.PhoneConfirmationView.AnonymousClass5 anonymousClass5, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0 = anonymousClass5;
        this.f$1 = tLRPC$TL_error;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$run$0(this.f$1);
    }
}
