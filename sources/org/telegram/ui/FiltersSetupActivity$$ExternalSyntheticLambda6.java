package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class FiltersSetupActivity$$ExternalSyntheticLambda6 implements RequestDelegate {
    public static final /* synthetic */ FiltersSetupActivity$$ExternalSyntheticLambda6 INSTANCE = new FiltersSetupActivity$$ExternalSyntheticLambda6();

    private /* synthetic */ FiltersSetupActivity$$ExternalSyntheticLambda6() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        FiltersSetupActivity.lambda$onFragmentDestroy$1(tLObject, tLRPC$TL_error);
    }
}
