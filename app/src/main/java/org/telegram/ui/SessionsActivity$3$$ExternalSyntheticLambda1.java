package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_authorization;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.ui.SessionsActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class SessionsActivity$3$$ExternalSyntheticLambda1 implements RequestDelegate {
    public final /* synthetic */ SessionsActivity.AnonymousClass3 f$0;
    public final /* synthetic */ TLRPC$TL_authorization f$1;

    public /* synthetic */ SessionsActivity$3$$ExternalSyntheticLambda1(SessionsActivity.AnonymousClass3 anonymousClass3, TLRPC$TL_authorization tLRPC$TL_authorization) {
        this.f$0 = anonymousClass3;
        this.f$1 = tLRPC$TL_authorization;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$hide$1(this.f$1, tLObject, tLRPC$TL_error);
    }
}
