package org.telegram.ui;

import org.telegram.ui.CallLogActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class CallLogActivity$2$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ CallLogActivity.AnonymousClass2 f$0;
    public final /* synthetic */ CallLogActivity.CallLogRow f$1;

    public /* synthetic */ CallLogActivity$2$$ExternalSyntheticLambda0(CallLogActivity.AnonymousClass2 anonymousClass2, CallLogActivity.CallLogRow callLogRow) {
        this.f$0 = anonymousClass2;
        this.f$1 = callLogRow;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onScrolled$0(this.f$1);
    }
}
