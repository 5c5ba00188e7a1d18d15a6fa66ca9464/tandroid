package org.telegram.messenger;

import org.telegram.tgnet.TLObject;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda81 implements Runnable {
    public final /* synthetic */ MediaDataController f$0;
    public final /* synthetic */ TLObject f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ long f$3;

    public /* synthetic */ MediaDataController$$ExternalSyntheticLambda81(MediaDataController mediaDataController, TLObject tLObject, int i, long j) {
        this.f$0 = mediaDataController;
        this.f$1 = tLObject;
        this.f$2 = i;
        this.f$3 = j;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$loadStickers$68(this.f$1, this.f$2, this.f$3);
    }
}
