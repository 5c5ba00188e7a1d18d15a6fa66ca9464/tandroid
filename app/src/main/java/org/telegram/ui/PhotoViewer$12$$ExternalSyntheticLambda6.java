package org.telegram.ui;

import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Photo;
import org.telegram.ui.PhotoViewer;
/* loaded from: classes3.dex */
public final /* synthetic */ class PhotoViewer$12$$ExternalSyntheticLambda6 implements Runnable {
    public final /* synthetic */ PhotoViewer.AnonymousClass12 f$0;
    public final /* synthetic */ TLObject f$1;
    public final /* synthetic */ UserConfig f$2;
    public final /* synthetic */ TLRPC$Photo f$3;

    public /* synthetic */ PhotoViewer$12$$ExternalSyntheticLambda6(PhotoViewer.AnonymousClass12 anonymousClass12, TLObject tLObject, UserConfig userConfig, TLRPC$Photo tLRPC$Photo) {
        this.f$0 = anonymousClass12;
        this.f$1 = tLObject;
        this.f$2 = userConfig;
        this.f$3 = tLRPC$Photo;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onItemClick$7(this.f$1, this.f$2, this.f$3);
    }
}
