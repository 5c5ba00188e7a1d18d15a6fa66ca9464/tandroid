package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda194 implements RequestDelegate {
    public static final /* synthetic */ MediaDataController$$ExternalSyntheticLambda194 INSTANCE = new MediaDataController$$ExternalSyntheticLambda194();

    private /* synthetic */ MediaDataController$$ExternalSyntheticLambda194() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        MediaDataController.lambda$markFeaturedStickersByIdAsRead$50(tLObject, tLRPC$TL_error);
    }
}
