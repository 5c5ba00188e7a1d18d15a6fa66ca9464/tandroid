package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes.dex */
public final /* synthetic */ class ChannelBoostsController$$ExternalSyntheticLambda4 implements RequestDelegate {
    public static final /* synthetic */ ChannelBoostsController$$ExternalSyntheticLambda4 INSTANCE = new ChannelBoostsController$$ExternalSyntheticLambda4();

    private /* synthetic */ ChannelBoostsController$$ExternalSyntheticLambda4() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        ChannelBoostsController.lambda$applyBoost$4(tLObject, tLRPC$TL_error);
    }
}
