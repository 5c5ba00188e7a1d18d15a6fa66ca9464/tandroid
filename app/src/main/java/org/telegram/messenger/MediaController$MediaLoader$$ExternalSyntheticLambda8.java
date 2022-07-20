package org.telegram.messenger;

import org.telegram.messenger.MediaController;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaController$MediaLoader$$ExternalSyntheticLambda8 implements Runnable {
    public final /* synthetic */ MediaController.MediaLoader f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ MediaController$MediaLoader$$ExternalSyntheticLambda8(MediaController.MediaLoader mediaLoader, int i) {
        this.f$0 = mediaLoader;
        this.f$1 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$copyFile$8(this.f$1);
    }
}
