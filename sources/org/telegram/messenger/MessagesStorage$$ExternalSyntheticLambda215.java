package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.messenger.MessagesController;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda215 implements Comparator {
    public static final /* synthetic */ MessagesStorage$$ExternalSyntheticLambda215 INSTANCE = new MessagesStorage$$ExternalSyntheticLambda215();

    private /* synthetic */ MessagesStorage$$ExternalSyntheticLambda215() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$loadDialogFilters$49;
        lambda$loadDialogFilters$49 = MessagesStorage.lambda$loadDialogFilters$49((MessagesController.DialogFilter) obj, (MessagesController.DialogFilter) obj2);
        return lambda$loadDialogFilters$49;
    }
}
