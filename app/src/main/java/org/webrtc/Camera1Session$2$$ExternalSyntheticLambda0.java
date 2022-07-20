package org.webrtc;

import org.webrtc.Camera1Session;
/* loaded from: classes3.dex */
public final /* synthetic */ class Camera1Session$2$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ Camera1Session.AnonymousClass2 f$0;
    public final /* synthetic */ byte[] f$1;

    public /* synthetic */ Camera1Session$2$$ExternalSyntheticLambda0(Camera1Session.AnonymousClass2 anonymousClass2, byte[] bArr) {
        this.f$0 = anonymousClass2;
        this.f$1 = bArr;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onPreviewFrame$0(this.f$1);
    }
}
