package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.messenger.MessagesController;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda211 implements Comparator {
    public static final /* synthetic */ MessagesStorage$$ExternalSyntheticLambda211 INSTANCE = new MessagesStorage$$ExternalSyntheticLambda211();

    private /* synthetic */ MessagesStorage$$ExternalSyntheticLambda211() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$processLoadedFilterPeersInternal$51;
        lambda$processLoadedFilterPeersInternal$51 = MessagesStorage.lambda$processLoadedFilterPeersInternal$51((MessagesController.DialogFilter) obj, (MessagesController.DialogFilter) obj2);
        return lambda$processLoadedFilterPeersInternal$51;
    }
}
