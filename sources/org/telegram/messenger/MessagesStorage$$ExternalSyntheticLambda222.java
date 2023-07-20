package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.ui.Adapters.DialogsSearchAdapter;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda222 implements Comparator {
    public static final /* synthetic */ MessagesStorage$$ExternalSyntheticLambda222 INSTANCE = new MessagesStorage$$ExternalSyntheticLambda222();

    private /* synthetic */ MessagesStorage$$ExternalSyntheticLambda222() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$localSearch$219;
        lambda$localSearch$219 = MessagesStorage.lambda$localSearch$219((DialogsSearchAdapter.DialogSearchResult) obj, (DialogsSearchAdapter.DialogSearchResult) obj2);
        return lambda$localSearch$219;
    }
}
