package org.telegram.messenger;

import org.telegram.messenger.ImageLoader;
/* loaded from: classes.dex */
public final /* synthetic */ class ImageLoader$5$$ExternalSyntheticLambda7 implements Runnable {
    public final /* synthetic */ ImageLoader.AnonymousClass5 f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ int f$3;

    public /* synthetic */ ImageLoader$5$$ExternalSyntheticLambda7(ImageLoader.AnonymousClass5 anonymousClass5, String str, int i, int i2) {
        this.f$0 = anonymousClass5;
        this.f$1 = str;
        this.f$2 = i;
        this.f$3 = i2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$fileDidFailedLoad$6(this.f$1, this.f$2, this.f$3);
    }
}
