package org.telegram.messenger.camera;

import android.graphics.SurfaceTexture;
import org.telegram.messenger.camera.CameraView;

/* loaded from: classes3.dex */
public final /* synthetic */ class CameraView$CameraGLThread$$ExternalSyntheticLambda6 implements SurfaceTexture.OnFrameAvailableListener {
    public final /* synthetic */ CameraView.CameraGLThread f$0;

    public /* synthetic */ CameraView$CameraGLThread$$ExternalSyntheticLambda6(CameraView.CameraGLThread cameraGLThread) {
        this.f$0 = cameraGLThread;
    }

    @Override // android.graphics.SurfaceTexture.OnFrameAvailableListener
    public final void onFrameAvailable(SurfaceTexture surfaceTexture) {
        this.f$0.updTex(surfaceTexture);
    }
}
