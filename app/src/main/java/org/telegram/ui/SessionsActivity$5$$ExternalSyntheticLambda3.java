package org.telegram.ui;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.ui.SessionsActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class SessionsActivity$5$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ SessionsActivity.AnonymousClass5 f$0;
    public final /* synthetic */ TLObject f$1;
    public final /* synthetic */ TLRPC$TL_error f$2;
    public final /* synthetic */ Runnable f$3;

    public /* synthetic */ SessionsActivity$5$$ExternalSyntheticLambda3(SessionsActivity.AnonymousClass5 anonymousClass5, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error, Runnable runnable) {
        this.f$0 = anonymousClass5;
        this.f$1 = tLObject;
        this.f$2 = tLRPC$TL_error;
        this.f$3 = runnable;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$processQr$1(this.f$1, this.f$2, this.f$3);
    }
}
