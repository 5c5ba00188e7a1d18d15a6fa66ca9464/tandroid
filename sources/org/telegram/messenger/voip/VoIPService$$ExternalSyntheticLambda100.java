package org.telegram.messenger.voip;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class VoIPService$$ExternalSyntheticLambda100 implements RequestDelegate {
    public static final /* synthetic */ VoIPService$$ExternalSyntheticLambda100 INSTANCE = new VoIPService$$ExternalSyntheticLambda100();

    private /* synthetic */ VoIPService$$ExternalSyntheticLambda100() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        VoIPService.lambda$createGroupInstance$37(tLObject, tLRPC$TL_error);
    }
}
