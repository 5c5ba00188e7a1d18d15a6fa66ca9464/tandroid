package org.webrtc;
/* loaded from: classes3.dex */
public final /* synthetic */ class EglRenderer$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ EglRenderer f$0;

    public /* synthetic */ EglRenderer$$ExternalSyntheticLambda0(EglRenderer eglRenderer) {
        this.f$0 = eglRenderer;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.renderFrameOnRenderThread();
    }
}
