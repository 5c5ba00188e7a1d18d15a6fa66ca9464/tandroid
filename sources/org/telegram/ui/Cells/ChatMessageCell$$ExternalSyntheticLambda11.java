package org.telegram.ui.Cells;

import java.util.Comparator;
import org.telegram.ui.Cells.ChatMessageCell;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatMessageCell$$ExternalSyntheticLambda11 implements Comparator {
    public static final /* synthetic */ ChatMessageCell$$ExternalSyntheticLambda11 INSTANCE = new ChatMessageCell$$ExternalSyntheticLambda11();

    private /* synthetic */ ChatMessageCell$$ExternalSyntheticLambda11() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$setMessageContent$9;
        lambda$setMessageContent$9 = ChatMessageCell.lambda$setMessageContent$9((ChatMessageCell.PollButton) obj, (ChatMessageCell.PollButton) obj2);
        return lambda$setMessageContent$9;
    }
}
