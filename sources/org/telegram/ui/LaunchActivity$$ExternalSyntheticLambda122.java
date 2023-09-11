package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class LaunchActivity$$ExternalSyntheticLambda122 implements RequestDelegate {
    public static final /* synthetic */ LaunchActivity$$ExternalSyntheticLambda122 INSTANCE = new LaunchActivity$$ExternalSyntheticLambda122();

    private /* synthetic */ LaunchActivity$$ExternalSyntheticLambda122() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        LaunchActivity.lambda$runLinkRequest$52(tLObject, tLRPC$TL_error);
    }
}
