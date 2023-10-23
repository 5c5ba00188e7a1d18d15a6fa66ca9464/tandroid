package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.ui.Adapters.DialogsSearchAdapter;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda220 implements Comparator {
    public static final /* synthetic */ MessagesStorage$$ExternalSyntheticLambda220 INSTANCE = new MessagesStorage$$ExternalSyntheticLambda220();

    private /* synthetic */ MessagesStorage$$ExternalSyntheticLambda220() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$localSearch$217;
        lambda$localSearch$217 = MessagesStorage.lambda$localSearch$217((DialogsSearchAdapter.DialogSearchResult) obj, (DialogsSearchAdapter.DialogSearchResult) obj2);
        return lambda$localSearch$217;
    }
}
