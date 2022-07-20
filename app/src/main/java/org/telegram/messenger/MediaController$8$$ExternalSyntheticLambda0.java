package org.telegram.messenger;

import org.telegram.messenger.MediaController;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaController$8$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ MediaController.AnonymousClass8 f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ MediaController$8$$ExternalSyntheticLambda0(MediaController.AnonymousClass8 anonymousClass8, int i, int i2) {
        this.f$0 = anonymousClass8;
        this.f$1 = i;
        this.f$2 = i2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onStateChanged$0(this.f$1, this.f$2);
    }
}
