package org.telegram.messenger.voip;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class VoIPService$$ExternalSyntheticLambda99 implements RequestDelegate {
    public static final /* synthetic */ VoIPService$$ExternalSyntheticLambda99 INSTANCE = new VoIPService$$ExternalSyntheticLambda99();

    private /* synthetic */ VoIPService$$ExternalSyntheticLambda99() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        VoIPService.lambda$onSignalingData$60(tLObject, tLRPC$TL_error);
    }
}
