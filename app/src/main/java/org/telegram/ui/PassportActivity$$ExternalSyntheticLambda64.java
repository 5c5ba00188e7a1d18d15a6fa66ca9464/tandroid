package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class PassportActivity$$ExternalSyntheticLambda64 implements RequestDelegate {
    public final /* synthetic */ PassportActivity f$0;

    public /* synthetic */ PassportActivity$$ExternalSyntheticLambda64(PassportActivity passportActivity) {
        this.f$0 = passportActivity;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$createManageInterface$19(tLObject, tLRPC$TL_error);
    }
}
