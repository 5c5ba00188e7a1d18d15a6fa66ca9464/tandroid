package org.telegram.ui.Stories.recorder;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes4.dex */
public final /* synthetic */ class DualCameraView$$ExternalSyntheticLambda2 implements RequestDelegate {
    public static final /* synthetic */ DualCameraView$$ExternalSyntheticLambda2 INSTANCE = new DualCameraView$$ExternalSyntheticLambda2();

    private /* synthetic */ DualCameraView$$ExternalSyntheticLambda2() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        DualCameraView.lambda$log$0(tLObject, tLRPC$TL_error);
    }
}
