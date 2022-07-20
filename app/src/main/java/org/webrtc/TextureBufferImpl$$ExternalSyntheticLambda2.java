package org.webrtc;

import java.util.concurrent.Callable;
import org.webrtc.VideoFrame;
/* loaded from: classes3.dex */
public final /* synthetic */ class TextureBufferImpl$$ExternalSyntheticLambda2 implements Callable {
    public final /* synthetic */ TextureBufferImpl f$0;

    public /* synthetic */ TextureBufferImpl$$ExternalSyntheticLambda2(TextureBufferImpl textureBufferImpl) {
        this.f$0 = textureBufferImpl;
    }

    @Override // java.util.concurrent.Callable
    public final Object call() {
        VideoFrame.I420Buffer lambda$toI420$1;
        lambda$toI420$1 = this.f$0.lambda$toI420$1();
        return lambda$toI420$1;
    }
}
