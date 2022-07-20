package org.telegram.messenger;
/* loaded from: classes.dex */
public final /* synthetic */ class ImageLoader$$ExternalSyntheticLambda5 implements Runnable {
    public final /* synthetic */ ImageLoader f$0;
    public final /* synthetic */ String f$1;

    public /* synthetic */ ImageLoader$$ExternalSyntheticLambda5(ImageLoader imageLoader, String str) {
        this.f$0 = imageLoader;
        this.f$1 = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$artworkLoadError$9(this.f$1);
    }
}
