package org.telegram.ui;

import android.os.Bundle;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.ui.LoginActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda29 implements Runnable {
    public final /* synthetic */ LoginActivity.LoginActivitySmsView f$0;
    public final /* synthetic */ TLRPC$TL_error f$1;
    public final /* synthetic */ Bundle f$2;
    public final /* synthetic */ TLObject f$3;

    public /* synthetic */ LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda29(LoginActivity.LoginActivitySmsView loginActivitySmsView, TLRPC$TL_error tLRPC$TL_error, Bundle bundle, TLObject tLObject) {
        this.f$0 = loginActivitySmsView;
        this.f$1 = tLRPC$TL_error;
        this.f$2 = bundle;
        this.f$3 = tLObject;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$resendCode$8(this.f$1, this.f$2, this.f$3);
    }
}
