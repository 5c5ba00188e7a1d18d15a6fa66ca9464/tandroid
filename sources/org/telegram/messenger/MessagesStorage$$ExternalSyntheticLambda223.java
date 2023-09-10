package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.ui.Adapters.DialogsSearchAdapter;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda223 implements Comparator {
    public static final /* synthetic */ MessagesStorage$$ExternalSyntheticLambda223 INSTANCE = new MessagesStorage$$ExternalSyntheticLambda223();

    private /* synthetic */ MessagesStorage$$ExternalSyntheticLambda223() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$localSearch$220;
        lambda$localSearch$220 = MessagesStorage.lambda$localSearch$220((DialogsSearchAdapter.DialogSearchResult) obj, (DialogsSearchAdapter.DialogSearchResult) obj2);
        return lambda$localSearch$220;
    }
}
