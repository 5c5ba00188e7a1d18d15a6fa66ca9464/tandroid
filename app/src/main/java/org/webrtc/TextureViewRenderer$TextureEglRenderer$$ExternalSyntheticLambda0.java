package org.webrtc;

import org.webrtc.TextureViewRenderer;
/* loaded from: classes3.dex */
public final /* synthetic */ class TextureViewRenderer$TextureEglRenderer$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ TextureViewRenderer.TextureEglRenderer f$0;

    public /* synthetic */ TextureViewRenderer$TextureEglRenderer$$ExternalSyntheticLambda0(TextureViewRenderer.TextureEglRenderer textureEglRenderer) {
        this.f$0 = textureEglRenderer;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onFirstFrameRendered$0();
    }
}
