package org.telegram.ui;

import org.telegram.ui.SessionsActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class SessionsActivity$5$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ SessionsActivity.AnonymousClass5 f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ Runnable f$2;

    public /* synthetic */ SessionsActivity$5$$ExternalSyntheticLambda2(SessionsActivity.AnonymousClass5 anonymousClass5, String str, Runnable runnable) {
        this.f$0 = anonymousClass5;
        this.f$1 = str;
        this.f$2 = runnable;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$processQr$4(this.f$1, this.f$2);
    }
}
