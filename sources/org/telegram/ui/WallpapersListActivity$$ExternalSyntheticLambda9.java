package org.telegram.ui;

import org.telegram.ui.ThemePreviewActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class WallpapersListActivity$$ExternalSyntheticLambda9 implements ThemePreviewActivity.WallpaperActivityDelegate {
    public final /* synthetic */ WallpapersListActivity f$0;

    public /* synthetic */ WallpapersListActivity$$ExternalSyntheticLambda9(WallpapersListActivity wallpapersListActivity) {
        this.f$0 = wallpapersListActivity;
    }

    @Override // org.telegram.ui.ThemePreviewActivity.WallpaperActivityDelegate
    public final void didSetNewBackground() {
        this.f$0.removeSelfFromStack();
    }
}
