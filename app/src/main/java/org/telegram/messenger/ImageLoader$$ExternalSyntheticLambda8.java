package org.telegram.messenger;

import java.io.File;
/* loaded from: classes.dex */
public final /* synthetic */ class ImageLoader$$ExternalSyntheticLambda8 implements Runnable {
    public final /* synthetic */ ImageLoader f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ File f$3;

    public /* synthetic */ ImageLoader$$ExternalSyntheticLambda8(ImageLoader imageLoader, String str, int i, File file) {
        this.f$0 = imageLoader;
        this.f$1 = str;
        this.f$2 = i;
        this.f$3 = file;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$fileDidLoaded$10(this.f$1, this.f$2, this.f$3);
    }
}
