package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.ui.Adapters.DialogsSearchAdapter;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda214 implements Comparator {
    public static final /* synthetic */ MessagesStorage$$ExternalSyntheticLambda214 INSTANCE = new MessagesStorage$$ExternalSyntheticLambda214();

    private /* synthetic */ MessagesStorage$$ExternalSyntheticLambda214() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$localSearch$211;
        lambda$localSearch$211 = MessagesStorage.lambda$localSearch$211((DialogsSearchAdapter.DialogSearchResult) obj, (DialogsSearchAdapter.DialogSearchResult) obj2);
        return lambda$localSearch$211;
    }
}
