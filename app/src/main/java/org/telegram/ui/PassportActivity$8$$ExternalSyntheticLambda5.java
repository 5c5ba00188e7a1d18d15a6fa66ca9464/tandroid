package org.telegram.ui;

import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.ui.PassportActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class PassportActivity$8$$ExternalSyntheticLambda5 implements Runnable {
    public final /* synthetic */ PassportActivity.AnonymousClass8 f$0;
    public final /* synthetic */ TLRPC$TL_error f$1;

    public /* synthetic */ PassportActivity$8$$ExternalSyntheticLambda5(PassportActivity.AnonymousClass8 anonymousClass8, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0 = anonymousClass8;
        this.f$1 = tLRPC$TL_error;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$generateNewSecret$6(this.f$1);
    }
}
