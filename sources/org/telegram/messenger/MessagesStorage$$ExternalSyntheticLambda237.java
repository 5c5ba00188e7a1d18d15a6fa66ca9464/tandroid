package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.messenger.MessagesController;
/* loaded from: classes3.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda237 implements Comparator {
    public static final /* synthetic */ MessagesStorage$$ExternalSyntheticLambda237 INSTANCE = new MessagesStorage$$ExternalSyntheticLambda237();

    private /* synthetic */ MessagesStorage$$ExternalSyntheticLambda237() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$processLoadedFilterPeersInternal$66;
        lambda$processLoadedFilterPeersInternal$66 = MessagesStorage.lambda$processLoadedFilterPeersInternal$66((MessagesController.DialogFilter) obj, (MessagesController.DialogFilter) obj2);
        return lambda$processLoadedFilterPeersInternal$66;
    }
}
