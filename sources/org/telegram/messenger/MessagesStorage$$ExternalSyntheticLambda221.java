package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.messenger.MessagesController;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda221 implements Comparator {
    public static final /* synthetic */ MessagesStorage$$ExternalSyntheticLambda221 INSTANCE = new MessagesStorage$$ExternalSyntheticLambda221();

    private /* synthetic */ MessagesStorage$$ExternalSyntheticLambda221() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$processLoadedFilterPeersInternal$59;
        lambda$processLoadedFilterPeersInternal$59 = MessagesStorage.lambda$processLoadedFilterPeersInternal$59((MessagesController.DialogFilter) obj, (MessagesController.DialogFilter) obj2);
        return lambda$processLoadedFilterPeersInternal$59;
    }
}
