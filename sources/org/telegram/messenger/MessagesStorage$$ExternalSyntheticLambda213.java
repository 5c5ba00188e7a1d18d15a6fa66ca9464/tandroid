package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.messenger.MessagesController;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda213 implements Comparator {
    public static final /* synthetic */ MessagesStorage$$ExternalSyntheticLambda213 INSTANCE = new MessagesStorage$$ExternalSyntheticLambda213();

    private /* synthetic */ MessagesStorage$$ExternalSyntheticLambda213() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$loadDialogFilters$48;
        lambda$loadDialogFilters$48 = MessagesStorage.lambda$loadDialogFilters$48((MessagesController.DialogFilter) obj, (MessagesController.DialogFilter) obj2);
        return lambda$loadDialogFilters$48;
    }
}
