package org.telegram.ui.Components.Premium;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class LimitReachedBottomSheet$$ExternalSyntheticLambda8 implements RequestDelegate {
    public final /* synthetic */ LimitReachedBottomSheet f$0;

    public /* synthetic */ LimitReachedBottomSheet$$ExternalSyntheticLambda8(LimitReachedBottomSheet limitReachedBottomSheet) {
        this.f$0 = limitReachedBottomSheet;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$loadInactiveChannels$10(tLObject, tLRPC$TL_error);
    }
}
