package org.telegram.messenger;

import java.util.ArrayList;
/* loaded from: classes.dex */
public final /* synthetic */ class DownloadController$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ DownloadController f$0;
    public final /* synthetic */ ArrayList f$1;

    public /* synthetic */ DownloadController$$ExternalSyntheticLambda3(DownloadController downloadController, ArrayList arrayList) {
        this.f$0 = downloadController;
        this.f$1 = arrayList;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$deleteRecentFiles$13(this.f$1);
    }
}
