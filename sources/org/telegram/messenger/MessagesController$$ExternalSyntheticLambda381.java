package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda381 implements RequestDelegate {
    public static final /* synthetic */ MessagesController$$ExternalSyntheticLambda381 INSTANCE = new MessagesController$$ExternalSyntheticLambda381();

    private /* synthetic */ MessagesController$$ExternalSyntheticLambda381() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        MessagesController.lambda$deleteParticipantFromChat$264(tLObject, tLRPC$TL_error);
    }
}
