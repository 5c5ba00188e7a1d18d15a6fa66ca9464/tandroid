package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda425 implements RequestDelegate {
    public static final /* synthetic */ MessagesController$$ExternalSyntheticLambda425 INSTANCE = new MessagesController$$ExternalSyntheticLambda425();

    private /* synthetic */ MessagesController$$ExternalSyntheticLambda425() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        MessagesController.lambda$removeSuggestion$35(tLObject, tLRPC$TL_error);
    }
}
