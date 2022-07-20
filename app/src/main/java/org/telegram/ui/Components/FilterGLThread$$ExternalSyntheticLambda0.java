package org.telegram.ui.Components;

import android.graphics.SurfaceTexture;
/* loaded from: classes3.dex */
public final /* synthetic */ class FilterGLThread$$ExternalSyntheticLambda0 implements SurfaceTexture.OnFrameAvailableListener {
    public final /* synthetic */ FilterGLThread f$0;

    public /* synthetic */ FilterGLThread$$ExternalSyntheticLambda0(FilterGLThread filterGLThread) {
        this.f$0 = filterGLThread;
    }

    @Override // android.graphics.SurfaceTexture.OnFrameAvailableListener
    public final void onFrameAvailable(SurfaceTexture surfaceTexture) {
        this.f$0.lambda$initGL$1(surfaceTexture);
    }
}
