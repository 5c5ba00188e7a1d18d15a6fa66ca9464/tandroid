package org.telegram.ui.Business;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes.dex */
public final /* synthetic */ class QuickRepliesController$$ExternalSyntheticLambda24 implements RequestDelegate {
    public static final /* synthetic */ QuickRepliesController$$ExternalSyntheticLambda24 INSTANCE = new QuickRepliesController$$ExternalSyntheticLambda24();

    private /* synthetic */ QuickRepliesController$$ExternalSyntheticLambda24() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        QuickRepliesController.lambda$renameReply$11(tLObject, tLRPC$TL_error);
    }
}
