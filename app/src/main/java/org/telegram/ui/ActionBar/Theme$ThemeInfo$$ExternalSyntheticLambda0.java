package org.telegram.ui.ActionBar;

import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes3.dex */
public final /* synthetic */ class Theme$ThemeInfo$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ Theme.ThemeInfo f$0;

    public /* synthetic */ Theme$ThemeInfo$$ExternalSyntheticLambda0(Theme.ThemeInfo themeInfo) {
        this.f$0 = themeInfo;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.onFinishLoadingRemoteTheme();
    }
}
