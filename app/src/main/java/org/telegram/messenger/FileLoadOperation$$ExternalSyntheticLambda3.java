package org.telegram.messenger;
/* loaded from: classes.dex */
public final /* synthetic */ class FileLoadOperation$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ FileLoadOperation f$0;
    public final /* synthetic */ FileLoadOperationStream f$1;

    public /* synthetic */ FileLoadOperation$$ExternalSyntheticLambda3(FileLoadOperation fileLoadOperation, FileLoadOperationStream fileLoadOperationStream) {
        this.f$0 = fileLoadOperation;
        this.f$1 = fileLoadOperationStream;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$removeStreamListener$3(this.f$1);
    }
}
