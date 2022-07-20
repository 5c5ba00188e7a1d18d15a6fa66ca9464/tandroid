package org.telegram.messenger;
/* loaded from: classes.dex */
public final /* synthetic */ class FileLoadOperation$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ FileLoadOperation f$0;
    public final /* synthetic */ boolean f$1;

    public /* synthetic */ FileLoadOperation$$ExternalSyntheticLambda4(FileLoadOperation fileLoadOperation, boolean z) {
        this.f$0 = fileLoadOperation;
        this.f$1 = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onFinishLoadingFile$8(this.f$1);
    }
}
