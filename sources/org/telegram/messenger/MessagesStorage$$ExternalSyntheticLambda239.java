package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.ui.Adapters.DialogsSearchAdapter;
/* loaded from: classes3.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda239 implements Comparator {
    public static final /* synthetic */ MessagesStorage$$ExternalSyntheticLambda239 INSTANCE = new MessagesStorage$$ExternalSyntheticLambda239();

    private /* synthetic */ MessagesStorage$$ExternalSyntheticLambda239() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$localSearch$237;
        lambda$localSearch$237 = MessagesStorage.lambda$localSearch$237((DialogsSearchAdapter.DialogSearchResult) obj, (DialogsSearchAdapter.DialogSearchResult) obj2);
        return lambda$localSearch$237;
    }
}
