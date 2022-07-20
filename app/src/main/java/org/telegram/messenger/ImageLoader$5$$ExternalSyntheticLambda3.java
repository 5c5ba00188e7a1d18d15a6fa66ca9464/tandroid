package org.telegram.messenger;

import org.telegram.messenger.ImageLoader;
/* loaded from: classes.dex */
public final /* synthetic */ class ImageLoader$5$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ int f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ boolean f$2;

    public /* synthetic */ ImageLoader$5$$ExternalSyntheticLambda3(int i, String str, boolean z) {
        this.f$0 = i;
        this.f$1 = str;
        this.f$2 = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        ImageLoader.AnonymousClass5.lambda$fileDidFailedUpload$3(this.f$0, this.f$1, this.f$2);
    }
}
