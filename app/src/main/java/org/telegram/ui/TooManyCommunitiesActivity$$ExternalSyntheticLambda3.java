package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class TooManyCommunitiesActivity$$ExternalSyntheticLambda3 implements RequestDelegate {
    public final /* synthetic */ TooManyCommunitiesActivity f$0;

    public /* synthetic */ TooManyCommunitiesActivity$$ExternalSyntheticLambda3(TooManyCommunitiesActivity tooManyCommunitiesActivity) {
        this.f$0 = tooManyCommunitiesActivity;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$loadInactiveChannels$5(tLObject, tLRPC$TL_error);
    }
}
