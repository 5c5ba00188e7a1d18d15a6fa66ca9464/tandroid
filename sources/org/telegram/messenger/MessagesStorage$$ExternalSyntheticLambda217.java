package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.messenger.MessagesController;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda217 implements Comparator {
    public static final /* synthetic */ MessagesStorage$$ExternalSyntheticLambda217 INSTANCE = new MessagesStorage$$ExternalSyntheticLambda217();

    private /* synthetic */ MessagesStorage$$ExternalSyntheticLambda217() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$loadDialogFilters$55;
        lambda$loadDialogFilters$55 = MessagesStorage.lambda$loadDialogFilters$55((MessagesController.DialogFilter) obj, (MessagesController.DialogFilter) obj2);
        return lambda$loadDialogFilters$55;
    }
}
