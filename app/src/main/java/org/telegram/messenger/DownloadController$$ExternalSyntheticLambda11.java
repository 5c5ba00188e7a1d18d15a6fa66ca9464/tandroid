package org.telegram.messenger;

import org.telegram.tgnet.TLObject;
/* loaded from: classes.dex */
public final /* synthetic */ class DownloadController$$ExternalSyntheticLambda11 implements Runnable {
    public final /* synthetic */ DownloadController f$0;
    public final /* synthetic */ TLObject f$1;

    public /* synthetic */ DownloadController$$ExternalSyntheticLambda11(DownloadController downloadController, TLObject tLObject) {
        this.f$0 = downloadController;
        this.f$1 = tLObject;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$loadAutoDownloadConfig$1(this.f$1);
    }
}
