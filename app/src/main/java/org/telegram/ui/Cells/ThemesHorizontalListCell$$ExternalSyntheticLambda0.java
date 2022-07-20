package org.telegram.ui.Cells;

import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes3.dex */
public final /* synthetic */ class ThemesHorizontalListCell$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ ThemesHorizontalListCell f$0;
    public final /* synthetic */ Theme.ThemeInfo f$1;

    public /* synthetic */ ThemesHorizontalListCell$$ExternalSyntheticLambda0(ThemesHorizontalListCell themesHorizontalListCell, Theme.ThemeInfo themeInfo) {
        this.f$0 = themesHorizontalListCell;
        this.f$1 = themeInfo;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$didReceivedNotification$2(this.f$1);
    }
}
