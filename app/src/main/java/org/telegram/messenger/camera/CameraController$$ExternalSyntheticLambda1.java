package org.telegram.messenger.camera;

import android.hardware.Camera;
/* loaded from: classes.dex */
public final /* synthetic */ class CameraController$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ Camera f$0;
    public final /* synthetic */ CameraSession f$1;

    public /* synthetic */ CameraController$$ExternalSyntheticLambda1(Camera camera, CameraSession cameraSession) {
        this.f$0 = camera;
        this.f$1 = cameraSession;
    }

    @Override // java.lang.Runnable
    public final void run() {
        CameraController.lambda$stopVideoRecording$15(this.f$0, this.f$1);
    }
}
