package org.telegram.ui.Stories;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes4.dex */
public final /* synthetic */ class StoriesController$$ExternalSyntheticLambda31 implements RequestDelegate {
    public static final /* synthetic */ StoriesController$$ExternalSyntheticLambda31 INSTANCE = new StoriesController$$ExternalSyntheticLambda31();

    private /* synthetic */ StoriesController$$ExternalSyntheticLambda31() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        StoriesController.lambda$markStoryAsRead$17(tLObject, tLRPC$TL_error);
    }
}
