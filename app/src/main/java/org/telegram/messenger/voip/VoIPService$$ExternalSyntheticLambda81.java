package org.telegram.messenger.voip;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes.dex */
public final /* synthetic */ class VoIPService$$ExternalSyntheticLambda81 implements RequestDelegate {
    public final /* synthetic */ VoIPService f$0;

    public /* synthetic */ VoIPService$$ExternalSyntheticLambda81(VoIPService voIPService) {
        this.f$0 = voIPService;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$startGroupCall$22(tLObject, tLRPC$TL_error);
    }
}
