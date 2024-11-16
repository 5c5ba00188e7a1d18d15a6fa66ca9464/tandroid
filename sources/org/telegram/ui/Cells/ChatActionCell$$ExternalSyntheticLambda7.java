package org.telegram.ui.Cells;

import org.telegram.ui.Components.RLottieDrawable;

/* loaded from: classes4.dex */
public final /* synthetic */ class ChatActionCell$$ExternalSyntheticLambda7 implements Runnable {
    public final /* synthetic */ RLottieDrawable f$0;

    public /* synthetic */ ChatActionCell$$ExternalSyntheticLambda7(RLottieDrawable rLottieDrawable) {
        this.f$0 = rLottieDrawable;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.restart();
    }
}
