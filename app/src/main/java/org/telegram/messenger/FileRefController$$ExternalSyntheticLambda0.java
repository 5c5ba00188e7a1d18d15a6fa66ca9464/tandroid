package org.telegram.messenger;

import org.telegram.messenger.FileRefController;
/* loaded from: classes.dex */
public final /* synthetic */ class FileRefController$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ FileRefController f$0;
    public final /* synthetic */ FileRefController.Requester f$1;

    public /* synthetic */ FileRefController$$ExternalSyntheticLambda0(FileRefController fileRefController, FileRefController.Requester requester) {
        this.f$0 = fileRefController;
        this.f$1 = requester;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onUpdateObjectReference$25(this.f$1);
    }
}
