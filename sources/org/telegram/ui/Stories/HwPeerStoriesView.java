package org.telegram.ui.Stories;

import android.content.Context;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Stories.PeerStoriesView;

/* loaded from: classes5.dex */
abstract class HwPeerStoriesView extends PeerStoriesView {
    public HwPeerStoriesView(Context context, StoryViewer storyViewer, PeerStoriesView.SharedResources sharedResources, Theme.ResourcesProvider resourcesProvider) {
        super(context, storyViewer, sharedResources, resourcesProvider);
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
