package org.telegram.messenger.camera;

import android.graphics.SurfaceTexture;
import org.telegram.messenger.camera.CameraView;
/* loaded from: classes.dex */
public final /* synthetic */ class CameraView$CameraGLThread$$ExternalSyntheticLambda0 implements SurfaceTexture.OnFrameAvailableListener {
    public final /* synthetic */ CameraView.CameraGLThread f$0;

    public /* synthetic */ CameraView$CameraGLThread$$ExternalSyntheticLambda0(CameraView.CameraGLThread cameraGLThread) {
        this.f$0 = cameraGLThread;
    }

    @Override // android.graphics.SurfaceTexture.OnFrameAvailableListener
    public final void onFrameAvailable(SurfaceTexture surfaceTexture) {
        CameraView.CameraGLThread.$r8$lambda$F9EmatV_YtQBimKoGwdOzkKHixM(this.f$0, surfaceTexture);
    }
}
