package org.telegram.ui.Stories;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes4.dex */
public final /* synthetic */ class StoriesController$$ExternalSyntheticLambda22 implements RequestDelegate {
    public static final /* synthetic */ StoriesController$$ExternalSyntheticLambda22 INSTANCE = new StoriesController$$ExternalSyntheticLambda22();

    private /* synthetic */ StoriesController$$ExternalSyntheticLambda22() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        StoriesController.lambda$deleteStories$14(tLObject, tLRPC$TL_error);
    }
}
