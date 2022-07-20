package org.telegram.ui;

import java.util.Comparator;
import org.telegram.messenger.support.LongSparseIntArray;
/* loaded from: classes3.dex */
public final /* synthetic */ class FilterCreateActivity$$ExternalSyntheticLambda8 implements Comparator {
    public final /* synthetic */ LongSparseIntArray f$0;

    public /* synthetic */ FilterCreateActivity$$ExternalSyntheticLambda8(LongSparseIntArray longSparseIntArray) {
        this.f$0 = longSparseIntArray;
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$saveFilterToServer$11;
        lambda$saveFilterToServer$11 = FilterCreateActivity.lambda$saveFilterToServer$11(this.f$0, (Long) obj, (Long) obj2);
        return lambda$saveFilterToServer$11;
    }
}
