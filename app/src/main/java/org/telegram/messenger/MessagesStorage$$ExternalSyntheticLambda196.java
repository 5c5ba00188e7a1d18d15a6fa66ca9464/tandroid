package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.messenger.support.LongSparseIntArray;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda196 implements Comparator {
    public final /* synthetic */ LongSparseIntArray f$0;

    public /* synthetic */ MessagesStorage$$ExternalSyntheticLambda196(LongSparseIntArray longSparseIntArray) {
        this.f$0 = longSparseIntArray;
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$resetDialogs$68;
        lambda$resetDialogs$68 = MessagesStorage.lambda$resetDialogs$68(this.f$0, (Long) obj, (Long) obj2);
        return lambda$resetDialogs$68;
    }
}
