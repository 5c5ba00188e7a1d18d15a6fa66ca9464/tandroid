package org.telegram.ui.Stories;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes4.dex */
public final /* synthetic */ class StoriesController$$ExternalSyntheticLambda21 implements RequestDelegate {
    public static final /* synthetic */ StoriesController$$ExternalSyntheticLambda21 INSTANCE = new StoriesController$$ExternalSyntheticLambda21();

    private /* synthetic */ StoriesController$$ExternalSyntheticLambda21() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        StoriesController.lambda$deleteStory$13(tLObject, tLRPC$TL_error);
    }
}
