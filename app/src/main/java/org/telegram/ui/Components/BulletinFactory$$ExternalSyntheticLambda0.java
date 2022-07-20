package org.telegram.ui.Components;

import org.telegram.ui.Components.Bulletin;
/* loaded from: classes3.dex */
public final /* synthetic */ class BulletinFactory$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ Bulletin.LottieLayout f$0;

    public /* synthetic */ BulletinFactory$$ExternalSyntheticLambda0(Bulletin.LottieLayout lottieLayout) {
        this.f$0 = lottieLayout;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.performHapticFeedback(3, 2);
    }
}
