package org.telegram.ui.Components;

import org.telegram.ui.Components.TranslateAlert;
/* loaded from: classes3.dex */
public final /* synthetic */ class TranslateAlert$$ExternalSyntheticLambda11 implements Runnable {
    public final /* synthetic */ TranslateAlert f$0;
    public final /* synthetic */ CharSequence f$1;
    public final /* synthetic */ TranslateAlert.OnTranslationSuccess f$2;
    public final /* synthetic */ long f$3;
    public final /* synthetic */ TranslateAlert.OnTranslationFail f$4;

    public /* synthetic */ TranslateAlert$$ExternalSyntheticLambda11(TranslateAlert translateAlert, CharSequence charSequence, TranslateAlert.OnTranslationSuccess onTranslationSuccess, long j, TranslateAlert.OnTranslationFail onTranslationFail) {
        this.f$0 = translateAlert;
        this.f$1 = charSequence;
        this.f$2 = onTranslationSuccess;
        this.f$3 = j;
        this.f$4 = onTranslationFail;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$fetchTranslation$12(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
