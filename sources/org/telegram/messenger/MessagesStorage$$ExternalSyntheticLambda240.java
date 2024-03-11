package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.ui.Adapters.DialogsSearchAdapter;
/* loaded from: classes3.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda240 implements Comparator {
    public static final /* synthetic */ MessagesStorage$$ExternalSyntheticLambda240 INSTANCE = new MessagesStorage$$ExternalSyntheticLambda240();

    private /* synthetic */ MessagesStorage$$ExternalSyntheticLambda240() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$localSearch$238;
        lambda$localSearch$238 = MessagesStorage.lambda$localSearch$238((DialogsSearchAdapter.DialogSearchResult) obj, (DialogsSearchAdapter.DialogSearchResult) obj2);
        return lambda$localSearch$238;
    }
}
