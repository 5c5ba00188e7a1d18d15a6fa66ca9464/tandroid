package org.telegram.messenger;

import androidx.core.util.Consumer;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda230 implements RequestDelegate {
    public final /* synthetic */ Consumer f$0;

    public /* synthetic */ MessagesController$$ExternalSyntheticLambda230(Consumer consumer) {
        this.f$0 = consumer;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        MessagesController.lambda$getNextReactionMention$4(this.f$0, tLObject, tLRPC$TL_error);
    }
}
