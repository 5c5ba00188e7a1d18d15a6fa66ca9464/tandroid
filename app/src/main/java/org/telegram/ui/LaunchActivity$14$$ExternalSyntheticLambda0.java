package org.telegram.ui;

import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.LaunchActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class LaunchActivity$14$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ LaunchActivity.AnonymousClass14 f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ long f$2;
    public final /* synthetic */ BaseFragment f$3;

    public /* synthetic */ LaunchActivity$14$$ExternalSyntheticLambda0(LaunchActivity.AnonymousClass14 anonymousClass14, String str, long j, BaseFragment baseFragment) {
        this.f$0 = anonymousClass14;
        this.f$1 = str;
        this.f$2 = j;
        this.f$3 = baseFragment;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onMessagesLoaded$2(this.f$1, this.f$2, this.f$3);
    }
}
