package org.telegram.ui.Components;

import android.graphics.SurfaceTexture;
/* loaded from: classes3.dex */
public final /* synthetic */ class InstantCameraView$$ExternalSyntheticLambda5 implements Runnable {
    public final /* synthetic */ InstantCameraView f$0;
    public final /* synthetic */ SurfaceTexture f$1;

    public /* synthetic */ InstantCameraView$$ExternalSyntheticLambda5(InstantCameraView instantCameraView, SurfaceTexture surfaceTexture) {
        this.f$0 = instantCameraView;
        this.f$1 = surfaceTexture;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$createCamera$5(this.f$1);
    }
}
