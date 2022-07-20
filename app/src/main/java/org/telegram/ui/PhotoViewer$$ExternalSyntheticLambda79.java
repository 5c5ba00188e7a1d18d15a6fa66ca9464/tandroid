package org.telegram.ui;

import org.telegram.messenger.MediaController;
import org.telegram.ui.Components.FilterGLThread;
import org.telegram.ui.Components.VideoEditTextureView;
/* loaded from: classes3.dex */
public final /* synthetic */ class PhotoViewer$$ExternalSyntheticLambda79 implements VideoEditTextureView.VideoEditTextureViewDelegate {
    public final /* synthetic */ MediaController.SavedFilterState f$0;

    public /* synthetic */ PhotoViewer$$ExternalSyntheticLambda79(MediaController.SavedFilterState savedFilterState) {
        this.f$0 = savedFilterState;
    }

    @Override // org.telegram.ui.Components.VideoEditTextureView.VideoEditTextureViewDelegate
    public final void onEGLThreadAvailable(FilterGLThread filterGLThread) {
        PhotoViewer.lambda$createVideoTextureView$50(this.f$0, filterGLThread);
    }
}
