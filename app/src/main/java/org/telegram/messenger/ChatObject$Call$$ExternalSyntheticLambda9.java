package org.telegram.messenger;

import org.telegram.messenger.ChatObject;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes.dex */
public final /* synthetic */ class ChatObject$Call$$ExternalSyntheticLambda9 implements RequestDelegate {
    public final /* synthetic */ ChatObject.Call f$0;

    public /* synthetic */ ChatObject$Call$$ExternalSyntheticLambda9(ChatObject.Call call) {
        this.f$0 = call;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$reloadGroupCall$9(tLObject, tLRPC$TL_error);
    }
}
