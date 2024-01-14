package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda231 implements RequestDelegate {
    public static final /* synthetic */ MediaDataController$$ExternalSyntheticLambda231 INSTANCE = new MediaDataController$$ExternalSyntheticLambda231();

    private /* synthetic */ MediaDataController$$ExternalSyntheticLambda231() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        MediaDataController.lambda$removeInline$142(tLObject, tLRPC$TL_error);
    }
}
