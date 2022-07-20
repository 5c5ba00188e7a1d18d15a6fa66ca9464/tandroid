package org.telegram.messenger.camera;

import org.telegram.messenger.camera.CameraView;
/* loaded from: classes.dex */
public final /* synthetic */ class CameraView$VideoRecorder$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ CameraView.VideoRecorder f$0;

    public /* synthetic */ CameraView$VideoRecorder$$ExternalSyntheticLambda0(CameraView.VideoRecorder videoRecorder) {
        this.f$0 = videoRecorder;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$handleStopRecording$0();
    }
}
