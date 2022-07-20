package org.telegram.ui;

import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.ui.PassportActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class PassportActivity$20$1$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ PassportActivity.ErrorRunnable f$0;
    public final /* synthetic */ TLRPC$TL_error f$1;
    public final /* synthetic */ String f$2;

    public /* synthetic */ PassportActivity$20$1$$ExternalSyntheticLambda2(PassportActivity.ErrorRunnable errorRunnable, TLRPC$TL_error tLRPC$TL_error, String str) {
        this.f$0 = errorRunnable;
        this.f$1 = tLRPC$TL_error;
        this.f$2 = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        PassportActivity.AnonymousClass20.AnonymousClass1.lambda$run$3(this.f$0, this.f$1, this.f$2);
    }
}
