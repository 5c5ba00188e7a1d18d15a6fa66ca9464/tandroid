package org.telegram.ui.Stories;

import android.content.Context;
import org.telegram.ui.Components.AvatarsImageView;

/* loaded from: classes4.dex */
class HwAvatarsImageView extends AvatarsImageView {
    public HwAvatarsImageView(Context context, boolean z) {
        super(context, z);
    }

    @Override // android.view.View
    public void invalidate() {
        if (HwFrameLayout.hwEnabled) {
            HwFrameLayout.hwViews.add(this);
        } else {
            super.invalidate();
        }
    }

    @Override // android.view.View
    public void invalidate(int i, int i2, int i3, int i4) {
        if (HwFrameLayout.hwEnabled) {
            HwFrameLayout.hwViews.add(this);
        } else {
            super.invalidate(i, i2, i3, i4);
        }
    }
}
