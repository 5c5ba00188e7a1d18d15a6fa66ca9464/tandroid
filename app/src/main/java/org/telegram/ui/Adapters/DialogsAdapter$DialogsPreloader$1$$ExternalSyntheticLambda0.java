package org.telegram.ui.Adapters;

import org.telegram.ui.Adapters.DialogsAdapter;
/* loaded from: classes3.dex */
public final /* synthetic */ class DialogsAdapter$DialogsPreloader$1$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ DialogsAdapter.DialogsPreloader.AnonymousClass1 f$0;
    public final /* synthetic */ long f$1;

    public /* synthetic */ DialogsAdapter$DialogsPreloader$1$$ExternalSyntheticLambda0(DialogsAdapter.DialogsPreloader.AnonymousClass1 anonymousClass1, long j) {
        this.f$0 = anonymousClass1;
        this.f$1 = j;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onError$1(this.f$1);
    }
}
