package org.telegram.ui.Components;

import android.graphics.SurfaceTexture;
import org.telegram.ui.Components.InstantCameraView;
/* loaded from: classes3.dex */
public final /* synthetic */ class InstantCameraView$CameraGLThread$$ExternalSyntheticLambda1 implements SurfaceTexture.OnFrameAvailableListener {
    public final /* synthetic */ InstantCameraView.CameraGLThread f$0;

    public /* synthetic */ InstantCameraView$CameraGLThread$$ExternalSyntheticLambda1(InstantCameraView.CameraGLThread cameraGLThread) {
        this.f$0 = cameraGLThread;
    }

    @Override // android.graphics.SurfaceTexture.OnFrameAvailableListener
    public final void onFrameAvailable(SurfaceTexture surfaceTexture) {
        this.f$0.lambda$handleMessage$1(surfaceTexture);
    }
}
