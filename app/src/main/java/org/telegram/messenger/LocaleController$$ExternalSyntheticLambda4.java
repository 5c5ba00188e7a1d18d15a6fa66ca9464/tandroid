package org.telegram.messenger;

import org.telegram.messenger.LocaleController;
/* loaded from: classes.dex */
public final /* synthetic */ class LocaleController$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ LocaleController f$0;
    public final /* synthetic */ LocaleController.LocaleInfo f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ LocaleController$$ExternalSyntheticLambda4(LocaleController localeController, LocaleController.LocaleInfo localeInfo, int i) {
        this.f$0 = localeController;
        this.f$1 = localeInfo;
        this.f$2 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$applyLanguage$2(this.f$1, this.f$2);
    }
}
