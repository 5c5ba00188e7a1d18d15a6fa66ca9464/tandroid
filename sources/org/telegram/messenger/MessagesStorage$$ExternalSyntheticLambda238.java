package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.messenger.MessagesController;
/* loaded from: classes3.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda238 implements Comparator {
    public static final /* synthetic */ MessagesStorage$$ExternalSyntheticLambda238 INSTANCE = new MessagesStorage$$ExternalSyntheticLambda238();

    private /* synthetic */ MessagesStorage$$ExternalSyntheticLambda238() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$loadDialogFilters$62;
        lambda$loadDialogFilters$62 = MessagesStorage.lambda$loadDialogFilters$62((MessagesController.DialogFilter) obj, (MessagesController.DialogFilter) obj2);
        return lambda$loadDialogFilters$62;
    }
}
