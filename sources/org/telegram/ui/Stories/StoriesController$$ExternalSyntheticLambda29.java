package org.telegram.ui.Stories;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes4.dex */
public final /* synthetic */ class StoriesController$$ExternalSyntheticLambda29 implements RequestDelegate {
    public static final /* synthetic */ StoriesController$$ExternalSyntheticLambda29 INSTANCE = new StoriesController$$ExternalSyntheticLambda29();

    private /* synthetic */ StoriesController$$ExternalSyntheticLambda29() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        StoriesController.lambda$setStoryReaction$21(tLObject, tLRPC$TL_error);
    }
}
