package org.telegram.messenger;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaController$$ExternalSyntheticLambda29 implements Runnable {
    public final /* synthetic */ MediaController f$0;
    public final /* synthetic */ MessageObject f$1;
    public final /* synthetic */ float f$2;

    public /* synthetic */ MediaController$$ExternalSyntheticLambda29(MediaController mediaController, MessageObject messageObject, float f) {
        this.f$0 = mediaController;
        this.f$1 = messageObject;
        this.f$2 = f;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$setPlaybackSpeed$15(this.f$1, this.f$2);
    }
}
