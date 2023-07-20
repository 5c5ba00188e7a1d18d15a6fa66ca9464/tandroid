package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.messenger.MessagesController;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda220 implements Comparator {
    public static final /* synthetic */ MessagesStorage$$ExternalSyntheticLambda220 INSTANCE = new MessagesStorage$$ExternalSyntheticLambda220();

    private /* synthetic */ MessagesStorage$$ExternalSyntheticLambda220() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$loadDialogFilters$54;
        lambda$loadDialogFilters$54 = MessagesStorage.lambda$loadDialogFilters$54((MessagesController.DialogFilter) obj, (MessagesController.DialogFilter) obj2);
        return lambda$loadDialogFilters$54;
    }
}
