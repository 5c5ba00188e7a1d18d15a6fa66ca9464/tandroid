package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.ui.Adapters.DialogsSearchAdapter;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda221 implements Comparator {
    public static final /* synthetic */ MessagesStorage$$ExternalSyntheticLambda221 INSTANCE = new MessagesStorage$$ExternalSyntheticLambda221();

    private /* synthetic */ MessagesStorage$$ExternalSyntheticLambda221() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$localSearch$218;
        lambda$localSearch$218 = MessagesStorage.lambda$localSearch$218((DialogsSearchAdapter.DialogSearchResult) obj, (DialogsSearchAdapter.DialogSearchResult) obj2);
        return lambda$localSearch$218;
    }
}
