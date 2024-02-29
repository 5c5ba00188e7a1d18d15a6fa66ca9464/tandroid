package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda234 implements RequestDelegate {
    public static final /* synthetic */ MediaDataController$$ExternalSyntheticLambda234 INSTANCE = new MediaDataController$$ExternalSyntheticLambda234();

    private /* synthetic */ MediaDataController$$ExternalSyntheticLambda234() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        MediaDataController.lambda$markFeaturedStickersAsRead$59(tLObject, tLRPC$TL_error);
    }
}
