package org.telegram.ui;

import j$.util.function.ToLongFunction;
import org.telegram.ui.StatisticActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class StatisticActivity$$ExternalSyntheticLambda5 implements ToLongFunction {
    public static final /* synthetic */ StatisticActivity$$ExternalSyntheticLambda5 INSTANCE = new StatisticActivity$$ExternalSyntheticLambda5();

    private /* synthetic */ StatisticActivity$$ExternalSyntheticLambda5() {
    }

    @Override // j$.util.function.ToLongFunction
    public final long applyAsLong(Object obj) {
        return ((StatisticActivity.RecentPostInfo) obj).getDate();
    }
}
