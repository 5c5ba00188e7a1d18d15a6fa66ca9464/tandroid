package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda238 implements RequestDelegate {
    public static final /* synthetic */ MediaDataController$$ExternalSyntheticLambda238 INSTANCE = new MediaDataController$$ExternalSyntheticLambda238();

    private /* synthetic */ MediaDataController$$ExternalSyntheticLambda238() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        MediaDataController.lambda$saveDraft$180(tLObject, tLRPC$TL_error);
    }
}
