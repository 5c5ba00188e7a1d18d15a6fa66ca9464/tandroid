package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.ui.Adapters.DialogsSearchAdapter;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda217 implements Comparator {
    public static final /* synthetic */ MessagesStorage$$ExternalSyntheticLambda217 INSTANCE = new MessagesStorage$$ExternalSyntheticLambda217();

    private /* synthetic */ MessagesStorage$$ExternalSyntheticLambda217() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$localSearch$214;
        lambda$localSearch$214 = MessagesStorage.lambda$localSearch$214((DialogsSearchAdapter.DialogSearchResult) obj, (DialogsSearchAdapter.DialogSearchResult) obj2);
        return lambda$localSearch$214;
    }
}
