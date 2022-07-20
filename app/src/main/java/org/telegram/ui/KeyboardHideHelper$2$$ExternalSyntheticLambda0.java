package org.telegram.ui;

import org.telegram.ui.ActionBar.AdjustPanLayoutHelper;
import org.telegram.ui.KeyboardHideHelper;
/* loaded from: classes3.dex */
public final /* synthetic */ class KeyboardHideHelper$2$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ KeyboardHideHelper.AnonymousClass2 f$0;
    public final /* synthetic */ AdjustPanLayoutHelper f$1;

    public /* synthetic */ KeyboardHideHelper$2$$ExternalSyntheticLambda0(KeyboardHideHelper.AnonymousClass2 anonymousClass2, AdjustPanLayoutHelper adjustPanLayoutHelper) {
        this.f$0 = anonymousClass2;
        this.f$1 = adjustPanLayoutHelper;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onAnimationEnd$0(this.f$1);
    }
}
