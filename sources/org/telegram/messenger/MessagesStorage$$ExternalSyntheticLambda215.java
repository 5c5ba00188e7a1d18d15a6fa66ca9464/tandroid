package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.ui.Adapters.DialogsSearchAdapter;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda215 implements Comparator {
    public static final /* synthetic */ MessagesStorage$$ExternalSyntheticLambda215 INSTANCE = new MessagesStorage$$ExternalSyntheticLambda215();

    private /* synthetic */ MessagesStorage$$ExternalSyntheticLambda215() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$localSearch$212;
        lambda$localSearch$212 = MessagesStorage.lambda$localSearch$212((DialogsSearchAdapter.DialogSearchResult) obj, (DialogsSearchAdapter.DialogSearchResult) obj2);
        return lambda$localSearch$212;
    }
}
