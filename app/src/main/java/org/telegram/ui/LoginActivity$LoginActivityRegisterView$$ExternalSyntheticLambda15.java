package org.telegram.ui;

import org.telegram.tgnet.TLObject;
import org.telegram.ui.LoginActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class LoginActivity$LoginActivityRegisterView$$ExternalSyntheticLambda15 implements Runnable {
    public final /* synthetic */ LoginActivity.LoginActivityRegisterView f$0;
    public final /* synthetic */ TLObject f$1;

    public /* synthetic */ LoginActivity$LoginActivityRegisterView$$ExternalSyntheticLambda15(LoginActivity.LoginActivityRegisterView loginActivityRegisterView, TLObject tLObject) {
        this.f$0 = loginActivityRegisterView;
        this.f$1 = tLObject;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onNextPressed$17(this.f$1);
    }
}
