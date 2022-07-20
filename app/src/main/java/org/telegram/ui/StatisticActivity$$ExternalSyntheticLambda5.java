package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class StatisticActivity$$ExternalSyntheticLambda5 implements RequestDelegate {
    public final /* synthetic */ StatisticActivity f$0;

    public /* synthetic */ StatisticActivity$$ExternalSyntheticLambda5(StatisticActivity statisticActivity) {
        this.f$0 = statisticActivity;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$onFragmentCreate$2(tLObject, tLRPC$TL_error);
    }
}
