package org.webrtc;

import android.os.Looper;
/* loaded from: classes3.dex */
public final /* synthetic */ class EglRenderer$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ EglRenderer f$0;
    public final /* synthetic */ Looper f$1;

    public /* synthetic */ EglRenderer$$ExternalSyntheticLambda2(EglRenderer eglRenderer, Looper looper) {
        this.f$0 = eglRenderer;
        this.f$1 = looper;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$release$2(this.f$1);
    }
}
