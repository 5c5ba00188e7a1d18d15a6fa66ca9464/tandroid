package org.telegram.ui.Cells;

import android.view.View;
import org.telegram.ui.Cells.WallpaperCell;
/* loaded from: classes3.dex */
public final /* synthetic */ class WallpaperCell$$ExternalSyntheticLambda1 implements View.OnLongClickListener {
    public final /* synthetic */ WallpaperCell f$0;
    public final /* synthetic */ WallpaperCell.WallpaperView f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ WallpaperCell$$ExternalSyntheticLambda1(WallpaperCell wallpaperCell, WallpaperCell.WallpaperView wallpaperView, int i) {
        this.f$0 = wallpaperCell;
        this.f$1 = wallpaperView;
        this.f$2 = i;
    }

    @Override // android.view.View.OnLongClickListener
    public final boolean onLongClick(View view) {
        boolean lambda$new$1;
        lambda$new$1 = this.f$0.lambda$new$1(this.f$1, this.f$2, view);
        return lambda$new$1;
    }
}
