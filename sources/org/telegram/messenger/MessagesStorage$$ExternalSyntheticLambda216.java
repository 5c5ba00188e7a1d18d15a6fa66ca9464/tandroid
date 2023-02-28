package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.ui.Adapters.DialogsSearchAdapter;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda216 implements Comparator {
    public static final /* synthetic */ MessagesStorage$$ExternalSyntheticLambda216 INSTANCE = new MessagesStorage$$ExternalSyntheticLambda216();

    private /* synthetic */ MessagesStorage$$ExternalSyntheticLambda216() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$localSearch$213;
        lambda$localSearch$213 = MessagesStorage.lambda$localSearch$213((DialogsSearchAdapter.DialogSearchResult) obj, (DialogsSearchAdapter.DialogSearchResult) obj2);
        return lambda$localSearch$213;
    }
}
