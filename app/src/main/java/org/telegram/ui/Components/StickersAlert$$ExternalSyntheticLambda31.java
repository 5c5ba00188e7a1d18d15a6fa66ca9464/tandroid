package org.telegram.ui.Components;

import org.telegram.messenger.MediaDataController;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class StickersAlert$$ExternalSyntheticLambda31 implements RequestDelegate {
    public final /* synthetic */ StickersAlert f$0;
    public final /* synthetic */ MediaDataController f$1;

    public /* synthetic */ StickersAlert$$ExternalSyntheticLambda31(StickersAlert stickersAlert, MediaDataController mediaDataController) {
        this.f$0 = stickersAlert;
        this.f$1 = mediaDataController;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$loadStickerSet$6(this.f$1, tLObject, tLRPC$TL_error);
    }
}
