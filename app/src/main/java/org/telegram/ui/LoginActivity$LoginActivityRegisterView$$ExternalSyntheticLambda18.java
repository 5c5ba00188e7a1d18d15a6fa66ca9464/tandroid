package org.telegram.ui;

import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.ui.LoginActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class LoginActivity$LoginActivityRegisterView$$ExternalSyntheticLambda18 implements Runnable {
    public final /* synthetic */ LoginActivity.LoginActivityRegisterView f$0;
    public final /* synthetic */ TLRPC$PhotoSize f$1;
    public final /* synthetic */ TLRPC$PhotoSize f$2;

    public /* synthetic */ LoginActivity$LoginActivityRegisterView$$ExternalSyntheticLambda18(LoginActivity.LoginActivityRegisterView loginActivityRegisterView, TLRPC$PhotoSize tLRPC$PhotoSize, TLRPC$PhotoSize tLRPC$PhotoSize2) {
        this.f$0 = loginActivityRegisterView;
        this.f$1 = tLRPC$PhotoSize;
        this.f$2 = tLRPC$PhotoSize2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$didUploadPhoto$13(this.f$1, this.f$2);
    }
}
