package org.telegram.messenger;

import org.telegram.messenger.FileLoader;
/* loaded from: classes.dex */
public final /* synthetic */ class FileLoader$1$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ FileLoader.AnonymousClass1 f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ String f$2;
    public final /* synthetic */ boolean f$3;

    public /* synthetic */ FileLoader$1$$ExternalSyntheticLambda0(FileLoader.AnonymousClass1 anonymousClass1, boolean z, String str, boolean z2) {
        this.f$0 = anonymousClass1;
        this.f$1 = z;
        this.f$2 = str;
        this.f$3 = z2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$didFailedUploadingFile$1(this.f$1, this.f$2, this.f$3);
    }
}
