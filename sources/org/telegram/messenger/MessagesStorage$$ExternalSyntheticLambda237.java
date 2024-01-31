package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.ui.Adapters.DialogsSearchAdapter;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda237 implements Comparator {
    public static final /* synthetic */ MessagesStorage$$ExternalSyntheticLambda237 INSTANCE = new MessagesStorage$$ExternalSyntheticLambda237();

    private /* synthetic */ MessagesStorage$$ExternalSyntheticLambda237() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$localSearch$235;
        lambda$localSearch$235 = MessagesStorage.lambda$localSearch$235((DialogsSearchAdapter.DialogSearchResult) obj, (DialogsSearchAdapter.DialogSearchResult) obj2);
        return lambda$localSearch$235;
    }
}
