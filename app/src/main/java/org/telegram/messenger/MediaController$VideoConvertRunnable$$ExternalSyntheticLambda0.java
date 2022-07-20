package org.telegram.messenger;

import org.telegram.messenger.MediaController;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaController$VideoConvertRunnable$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ MediaController.VideoConvertMessage f$0;

    public /* synthetic */ MediaController$VideoConvertRunnable$$ExternalSyntheticLambda0(MediaController.VideoConvertMessage videoConvertMessage) {
        this.f$0 = videoConvertMessage;
    }

    @Override // java.lang.Runnable
    public final void run() {
        MediaController.VideoConvertRunnable.lambda$runConversion$0(this.f$0);
    }
}
