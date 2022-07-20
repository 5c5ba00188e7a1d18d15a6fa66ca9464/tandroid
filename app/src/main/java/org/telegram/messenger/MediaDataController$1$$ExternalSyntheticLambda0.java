package org.telegram.messenger;

import org.telegram.messenger.MediaDataController;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaDataController$1$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ MediaDataController.AnonymousClass1 f$0;
    public final /* synthetic */ Runnable f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ MediaDataController$1$$ExternalSyntheticLambda0(MediaDataController.AnonymousClass1 anonymousClass1, Runnable runnable, int i) {
        this.f$0 = anonymousClass1;
        this.f$1 = runnable;
        this.f$2 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$run$0(this.f$1, this.f$2);
    }
}
