package org.telegram.messenger;

import org.telegram.messenger.ImageLoader;
/* loaded from: classes.dex */
public final /* synthetic */ class ImageLoader$HttpImageTask$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ ImageLoader.HttpImageTask f$0;
    public final /* synthetic */ long f$1;
    public final /* synthetic */ long f$2;

    public /* synthetic */ ImageLoader$HttpImageTask$$ExternalSyntheticLambda4(ImageLoader.HttpImageTask httpImageTask, long j, long j2) {
        this.f$0 = httpImageTask;
        this.f$1 = j;
        this.f$2 = j2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$reportProgress$1(this.f$1, this.f$2);
    }
}
