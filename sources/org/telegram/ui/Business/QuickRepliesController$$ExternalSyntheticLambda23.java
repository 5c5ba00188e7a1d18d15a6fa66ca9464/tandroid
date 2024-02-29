package org.telegram.ui.Business;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes.dex */
public final /* synthetic */ class QuickRepliesController$$ExternalSyntheticLambda23 implements RequestDelegate {
    public static final /* synthetic */ QuickRepliesController$$ExternalSyntheticLambda23 INSTANCE = new QuickRepliesController$$ExternalSyntheticLambda23();

    private /* synthetic */ QuickRepliesController$$ExternalSyntheticLambda23() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        QuickRepliesController.lambda$deleteReplies$13(tLObject, tLRPC$TL_error);
    }
}
