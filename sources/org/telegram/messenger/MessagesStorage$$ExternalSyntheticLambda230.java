package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.ui.Adapters.DialogsSearchAdapter;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda230 implements Comparator {
    public static final /* synthetic */ MessagesStorage$$ExternalSyntheticLambda230 INSTANCE = new MessagesStorage$$ExternalSyntheticLambda230();

    private /* synthetic */ MessagesStorage$$ExternalSyntheticLambda230() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$localSearch$228;
        lambda$localSearch$228 = MessagesStorage.lambda$localSearch$228((DialogsSearchAdapter.DialogSearchResult) obj, (DialogsSearchAdapter.DialogSearchResult) obj2);
        return lambda$localSearch$228;
    }
}
