package org.telegram.ui.Components;

import android.graphics.SurfaceTexture;
import org.telegram.ui.Components.FilterGLThread;
/* loaded from: classes3.dex */
public final /* synthetic */ class VideoEditTextureView$$ExternalSyntheticLambda1 implements FilterGLThread.FilterGLThreadVideoDelegate {
    public final /* synthetic */ VideoEditTextureView f$0;

    public /* synthetic */ VideoEditTextureView$$ExternalSyntheticLambda1(VideoEditTextureView videoEditTextureView) {
        this.f$0 = videoEditTextureView;
    }

    @Override // org.telegram.ui.Components.FilterGLThread.FilterGLThreadVideoDelegate
    public final void onVideoSurfaceCreated(SurfaceTexture surfaceTexture) {
        this.f$0.lambda$onSurfaceTextureAvailable$0(surfaceTexture);
    }
}
