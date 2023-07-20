package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.messenger.MessagesController;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda219 implements Comparator {
    public static final /* synthetic */ MessagesStorage$$ExternalSyntheticLambda219 INSTANCE = new MessagesStorage$$ExternalSyntheticLambda219();

    private /* synthetic */ MessagesStorage$$ExternalSyntheticLambda219() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$processLoadedFilterPeersInternal$58;
        lambda$processLoadedFilterPeersInternal$58 = MessagesStorage.lambda$processLoadedFilterPeersInternal$58((MessagesController.DialogFilter) obj, (MessagesController.DialogFilter) obj2);
        return lambda$processLoadedFilterPeersInternal$58;
    }
}
