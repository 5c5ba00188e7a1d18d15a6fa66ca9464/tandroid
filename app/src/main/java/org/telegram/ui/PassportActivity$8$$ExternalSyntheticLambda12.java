package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.ui.PassportActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class PassportActivity$8$$ExternalSyntheticLambda12 implements RequestDelegate {
    public final /* synthetic */ PassportActivity.AnonymousClass8 f$0;

    public /* synthetic */ PassportActivity$8$$ExternalSyntheticLambda12(PassportActivity.AnonymousClass8 anonymousClass8) {
        this.f$0 = anonymousClass8;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$resetSecret$1(tLObject, tLRPC$TL_error);
    }
}
