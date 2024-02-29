package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.messenger.MessagesController;
/* loaded from: classes3.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda236 implements Comparator {
    public static final /* synthetic */ MessagesStorage$$ExternalSyntheticLambda236 INSTANCE = new MessagesStorage$$ExternalSyntheticLambda236();

    private /* synthetic */ MessagesStorage$$ExternalSyntheticLambda236() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$processLoadedFilterPeersInternal$65;
        lambda$processLoadedFilterPeersInternal$65 = MessagesStorage.lambda$processLoadedFilterPeersInternal$65((MessagesController.DialogFilter) obj, (MessagesController.DialogFilter) obj2);
        return lambda$processLoadedFilterPeersInternal$65;
    }
}
