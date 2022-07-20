package org.telegram.messenger;

import org.telegram.messenger.MediaController;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaController$4$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ MediaController.AnonymousClass4 f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ MediaController$4$$ExternalSyntheticLambda0(MediaController.AnonymousClass4 anonymousClass4, int i) {
        this.f$0 = anonymousClass4;
        this.f$1 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onCallStateChanged$0(this.f$1);
    }
}
