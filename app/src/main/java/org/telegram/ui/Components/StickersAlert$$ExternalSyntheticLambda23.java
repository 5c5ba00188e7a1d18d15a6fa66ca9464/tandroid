package org.telegram.ui.Components;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class StickersAlert$$ExternalSyntheticLambda23 implements Runnable {
    public final /* synthetic */ StickersAlert f$0;
    public final /* synthetic */ TLRPC$TL_error f$1;
    public final /* synthetic */ TLObject f$2;

    public /* synthetic */ StickersAlert$$ExternalSyntheticLambda23(StickersAlert stickersAlert, TLRPC$TL_error tLRPC$TL_error, TLObject tLObject) {
        this.f$0 = stickersAlert;
        this.f$1 = tLRPC$TL_error;
        this.f$2 = tLObject;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$updateFields$14(this.f$1, this.f$2);
    }
}
