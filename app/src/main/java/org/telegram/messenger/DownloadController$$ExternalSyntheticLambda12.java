package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes.dex */
public final /* synthetic */ class DownloadController$$ExternalSyntheticLambda12 implements RequestDelegate {
    public final /* synthetic */ DownloadController f$0;

    public /* synthetic */ DownloadController$$ExternalSyntheticLambda12(DownloadController downloadController) {
        this.f$0 = downloadController;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$loadAutoDownloadConfig$2(tLObject, tLRPC$TL_error);
    }
}
