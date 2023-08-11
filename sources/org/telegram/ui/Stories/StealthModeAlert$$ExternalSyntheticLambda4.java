package org.telegram.ui.Stories;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes4.dex */
public final /* synthetic */ class StealthModeAlert$$ExternalSyntheticLambda4 implements RequestDelegate {
    public static final /* synthetic */ StealthModeAlert$$ExternalSyntheticLambda4 INSTANCE = new StealthModeAlert$$ExternalSyntheticLambda4();

    private /* synthetic */ StealthModeAlert$$ExternalSyntheticLambda4() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        StealthModeAlert.lambda$new$2(tLObject, tLRPC$TL_error);
    }
}
