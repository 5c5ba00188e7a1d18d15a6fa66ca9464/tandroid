package org.telegram.messenger;

import org.telegram.tgnet.TLRPC$Document;
/* loaded from: classes.dex */
public final /* synthetic */ class FileLoader$$ExternalSyntheticLambda7 implements Runnable {
    public final /* synthetic */ FileLoader f$0;
    public final /* synthetic */ TLRPC$Document f$1;
    public final /* synthetic */ boolean f$2;

    public /* synthetic */ FileLoader$$ExternalSyntheticLambda7(FileLoader fileLoader, TLRPC$Document tLRPC$Document, boolean z) {
        this.f$0 = fileLoader;
        this.f$1 = tLRPC$Document;
        this.f$2 = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$removeLoadingVideo$1(this.f$1, this.f$2);
    }
}
