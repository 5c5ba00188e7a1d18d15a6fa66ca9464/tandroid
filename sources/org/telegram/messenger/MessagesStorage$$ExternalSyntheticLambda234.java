package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.messenger.MessagesController;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda234 implements Comparator {
    public static final /* synthetic */ MessagesStorage$$ExternalSyntheticLambda234 INSTANCE = new MessagesStorage$$ExternalSyntheticLambda234();

    private /* synthetic */ MessagesStorage$$ExternalSyntheticLambda234() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$processLoadedFilterPeersInternal$64;
        lambda$processLoadedFilterPeersInternal$64 = MessagesStorage.lambda$processLoadedFilterPeersInternal$64((MessagesController.DialogFilter) obj, (MessagesController.DialogFilter) obj2);
        return lambda$processLoadedFilterPeersInternal$64;
    }
}
