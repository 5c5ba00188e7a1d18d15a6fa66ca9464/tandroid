package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class PrivacySettingsActivity$$ExternalSyntheticLambda11 implements RequestDelegate {
    public final /* synthetic */ PrivacySettingsActivity f$0;

    public /* synthetic */ PrivacySettingsActivity$$ExternalSyntheticLambda11(PrivacySettingsActivity privacySettingsActivity) {
        this.f$0 = privacySettingsActivity;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$loadPasswordSettings$17(tLObject, tLRPC$TL_error);
    }
}
