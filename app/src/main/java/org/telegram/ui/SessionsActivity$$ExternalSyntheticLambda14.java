package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class SessionsActivity$$ExternalSyntheticLambda14 implements RequestDelegate {
    public final /* synthetic */ SessionsActivity f$0;

    public /* synthetic */ SessionsActivity$$ExternalSyntheticLambda14(SessionsActivity sessionsActivity) {
        this.f$0 = sessionsActivity;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$createView$5(tLObject, tLRPC$TL_error);
    }
}
