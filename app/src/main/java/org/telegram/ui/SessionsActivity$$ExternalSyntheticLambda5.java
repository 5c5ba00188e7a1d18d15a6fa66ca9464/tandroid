package org.telegram.ui;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class SessionsActivity$$ExternalSyntheticLambda5 implements Runnable {
    public final /* synthetic */ SessionsActivity f$0;
    public final /* synthetic */ TLRPC$TL_error f$1;
    public final /* synthetic */ TLObject f$2;

    public /* synthetic */ SessionsActivity$$ExternalSyntheticLambda5(SessionsActivity sessionsActivity, TLRPC$TL_error tLRPC$TL_error, TLObject tLObject) {
        this.f$0 = sessionsActivity;
        this.f$1 = tLRPC$TL_error;
        this.f$2 = tLObject;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$createView$2(this.f$1, this.f$2);
    }
}
