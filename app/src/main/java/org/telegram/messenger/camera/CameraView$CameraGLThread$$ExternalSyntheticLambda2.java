package org.telegram.messenger.camera;

import org.telegram.messenger.camera.CameraView;
/* loaded from: classes.dex */
public final /* synthetic */ class CameraView$CameraGLThread$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ CameraView.CameraGLThread f$0;

    public /* synthetic */ CameraView$CameraGLThread$$ExternalSyntheticLambda2(CameraView.CameraGLThread cameraGLThread) {
        this.f$0 = cameraGLThread;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onDraw$1();
    }
}
