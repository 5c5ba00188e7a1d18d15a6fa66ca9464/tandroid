package org.telegram.ui.Components;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes4.dex */
public final /* synthetic */ class AlertsCreator$$ExternalSyntheticLambda106 implements RequestDelegate {
    public static final /* synthetic */ AlertsCreator$$ExternalSyntheticLambda106 INSTANCE = new AlertsCreator$$ExternalSyntheticLambda106();

    private /* synthetic */ AlertsCreator$$ExternalSyntheticLambda106() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AlertsCreator.lambda$createReportAlert$97(tLObject, tLRPC$TL_error);
    }
}
