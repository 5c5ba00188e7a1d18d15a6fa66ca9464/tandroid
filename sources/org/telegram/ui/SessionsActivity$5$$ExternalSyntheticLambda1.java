package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.ui.SessionsActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class SessionsActivity$5$$ExternalSyntheticLambda1 implements RequestDelegate {
    public static final /* synthetic */ SessionsActivity$5$$ExternalSyntheticLambda1 INSTANCE = new SessionsActivity$5$$ExternalSyntheticLambda1();

    private /* synthetic */ SessionsActivity$5$$ExternalSyntheticLambda1() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        SessionsActivity.5.lambda$onSessionTerminated$1(tLObject, tLRPC$TL_error);
    }
}
