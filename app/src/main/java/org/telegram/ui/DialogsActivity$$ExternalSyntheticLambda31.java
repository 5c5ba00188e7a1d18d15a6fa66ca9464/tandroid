package org.telegram.ui;

import org.telegram.messenger.AccountInstance;
/* loaded from: classes3.dex */
public final /* synthetic */ class DialogsActivity$$ExternalSyntheticLambda31 implements Runnable {
    public final /* synthetic */ AccountInstance f$0;

    public /* synthetic */ DialogsActivity$$ExternalSyntheticLambda31(AccountInstance accountInstance) {
        this.f$0 = accountInstance;
    }

    @Override // java.lang.Runnable
    public final void run() {
        DialogsActivity.lambda$loadDialogs$1(this.f$0);
    }
}
