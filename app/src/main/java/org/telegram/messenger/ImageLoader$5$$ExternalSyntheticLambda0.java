package org.telegram.messenger;

import org.telegram.messenger.ImageLoader;
/* loaded from: classes.dex */
public final /* synthetic */ class ImageLoader$5$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ long f$2;
    public final /* synthetic */ long f$3;

    public /* synthetic */ ImageLoader$5$$ExternalSyntheticLambda0(int i, String str, long j, long j2) {
        this.f$0 = i;
        this.f$1 = str;
        this.f$2 = j;
        this.f$3 = j2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        ImageLoader.AnonymousClass5.lambda$fileLoadProgressChanged$7(this.f$0, this.f$1, this.f$2, this.f$3);
    }
}
