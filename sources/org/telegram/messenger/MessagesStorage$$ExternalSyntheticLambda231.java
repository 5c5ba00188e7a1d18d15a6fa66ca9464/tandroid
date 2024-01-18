package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.ui.Adapters.DialogsSearchAdapter;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda231 implements Comparator {
    public static final /* synthetic */ MessagesStorage$$ExternalSyntheticLambda231 INSTANCE = new MessagesStorage$$ExternalSyntheticLambda231();

    private /* synthetic */ MessagesStorage$$ExternalSyntheticLambda231() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$localSearch$229;
        lambda$localSearch$229 = MessagesStorage.lambda$localSearch$229((DialogsSearchAdapter.DialogSearchResult) obj, (DialogsSearchAdapter.DialogSearchResult) obj2);
        return lambda$localSearch$229;
    }
}
