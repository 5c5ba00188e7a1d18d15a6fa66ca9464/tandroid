package org.telegram.ui.ActionBar;

import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes3.dex */
public final /* synthetic */ class Theme$PatternsLoader$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ Theme.PatternsLoader f$0;
    public final /* synthetic */ Theme.PatternsLoader.LoadingPattern f$1;

    public /* synthetic */ Theme$PatternsLoader$$ExternalSyntheticLambda2(Theme.PatternsLoader patternsLoader, Theme.PatternsLoader.LoadingPattern loadingPattern) {
        this.f$0 = patternsLoader;
        this.f$1 = loadingPattern;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$didReceivedNotification$3(this.f$1);
    }
}
