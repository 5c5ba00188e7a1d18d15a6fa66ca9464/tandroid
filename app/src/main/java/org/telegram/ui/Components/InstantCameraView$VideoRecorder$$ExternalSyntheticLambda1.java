package org.telegram.ui.Components;

import org.telegram.ui.Components.InstantCameraView;
/* loaded from: classes3.dex */
public final /* synthetic */ class InstantCameraView$VideoRecorder$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ InstantCameraView.VideoRecorder f$0;

    public /* synthetic */ InstantCameraView$VideoRecorder$$ExternalSyntheticLambda1(InstantCameraView.VideoRecorder videoRecorder) {
        this.f$0 = videoRecorder;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$handleVideoFrameAvailable$1();
    }
}
