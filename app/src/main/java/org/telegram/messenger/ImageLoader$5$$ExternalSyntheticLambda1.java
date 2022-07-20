package org.telegram.messenger;

import org.telegram.messenger.ImageLoader;
/* loaded from: classes.dex */
public final /* synthetic */ class ImageLoader$5$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ int f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ long f$2;
    public final /* synthetic */ long f$3;
    public final /* synthetic */ boolean f$4;

    public /* synthetic */ ImageLoader$5$$ExternalSyntheticLambda1(int i, String str, long j, long j2, boolean z) {
        this.f$0 = i;
        this.f$1 = str;
        this.f$2 = j;
        this.f$3 = j2;
        this.f$4 = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        ImageLoader.AnonymousClass5.lambda$fileUploadProgressChanged$0(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
