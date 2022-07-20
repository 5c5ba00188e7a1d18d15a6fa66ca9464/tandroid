package org.webrtc;

import android.graphics.SurfaceTexture;
/* loaded from: classes3.dex */
public final /* synthetic */ class SurfaceTextureHelper$$ExternalSyntheticLambda0 implements SurfaceTexture.OnFrameAvailableListener {
    public final /* synthetic */ SurfaceTextureHelper f$0;

    public /* synthetic */ SurfaceTextureHelper$$ExternalSyntheticLambda0(SurfaceTextureHelper surfaceTextureHelper) {
        this.f$0 = surfaceTextureHelper;
    }

    @Override // android.graphics.SurfaceTexture.OnFrameAvailableListener
    public final void onFrameAvailable(SurfaceTexture surfaceTexture) {
        this.f$0.lambda$new$0(surfaceTexture);
    }
}
