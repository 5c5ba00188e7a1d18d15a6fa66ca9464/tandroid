package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.ui.PassportActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class PassportActivity$PhoneConfirmationView$5$$ExternalSyntheticLambda2 implements RequestDelegate {
    public final /* synthetic */ PassportActivity.PhoneConfirmationView.AnonymousClass5 f$0;

    public /* synthetic */ PassportActivity$PhoneConfirmationView$5$$ExternalSyntheticLambda2(PassportActivity.PhoneConfirmationView.AnonymousClass5 anonymousClass5) {
        this.f$0 = anonymousClass5;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$run$1(tLObject, tLRPC$TL_error);
    }
}
