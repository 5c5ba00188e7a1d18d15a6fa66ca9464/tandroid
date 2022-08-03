package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda196 implements RequestDelegate {
    public static final /* synthetic */ MediaDataController$$ExternalSyntheticLambda196 INSTANCE = new MediaDataController$$ExternalSyntheticLambda196();

    private /* synthetic */ MediaDataController$$ExternalSyntheticLambda196() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        MediaDataController.lambda$markFeaturedStickersAsRead$50(tLObject, tLRPC$TL_error);
    }
}
