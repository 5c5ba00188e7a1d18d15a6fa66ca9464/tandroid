package org.telegram.ui.Components;

import org.telegram.ui.Components.TranslateAlert;
/* loaded from: classes3.dex */
public final /* synthetic */ class TranslateAlert$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ TranslateAlert.OnTranslationFail f$0;

    public /* synthetic */ TranslateAlert$$ExternalSyntheticLambda4(TranslateAlert.OnTranslationFail onTranslationFail) {
        this.f$0 = onTranslationFail;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.run(false);
    }
}
