package org.telegram.ui;

import org.telegram.tgnet.TLRPC$FileLocation;
import org.telegram.ui.LoginActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class LoginActivity$LoginActivityRegisterView$$ExternalSyntheticLambda17 implements Runnable {
    public final /* synthetic */ LoginActivity.LoginActivityRegisterView f$0;
    public final /* synthetic */ TLRPC$FileLocation f$1;

    public /* synthetic */ LoginActivity$LoginActivityRegisterView$$ExternalSyntheticLambda17(LoginActivity.LoginActivityRegisterView loginActivityRegisterView, TLRPC$FileLocation tLRPC$FileLocation) {
        this.f$0 = loginActivityRegisterView;
        this.f$1 = tLRPC$FileLocation;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onNextPressed$16(this.f$1);
    }
}
