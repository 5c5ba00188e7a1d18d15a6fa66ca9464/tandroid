package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.messenger.MessagesController;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda228 implements Comparator {
    public static final /* synthetic */ MessagesStorage$$ExternalSyntheticLambda228 INSTANCE = new MessagesStorage$$ExternalSyntheticLambda228();

    private /* synthetic */ MessagesStorage$$ExternalSyntheticLambda228() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$loadDialogFilters$60;
        lambda$loadDialogFilters$60 = MessagesStorage.lambda$loadDialogFilters$60((MessagesController.DialogFilter) obj, (MessagesController.DialogFilter) obj2);
        return lambda$loadDialogFilters$60;
    }
}
