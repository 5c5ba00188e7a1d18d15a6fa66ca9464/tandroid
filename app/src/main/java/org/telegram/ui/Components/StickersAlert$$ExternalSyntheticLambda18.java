package org.telegram.ui.Components;

import android.widget.TextView;
/* loaded from: classes3.dex */
public final /* synthetic */ class StickersAlert$$ExternalSyntheticLambda18 implements Runnable {
    public final /* synthetic */ StickersAlert f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ TextView f$2;

    public /* synthetic */ StickersAlert$$ExternalSyntheticLambda18(StickersAlert stickersAlert, String str, TextView textView) {
        this.f$0 = stickersAlert;
        this.f$1 = str;
        this.f$2 = textView;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$checkUrlAvailable$33(this.f$1, this.f$2);
    }
}
