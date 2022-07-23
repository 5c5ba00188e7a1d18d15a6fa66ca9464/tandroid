package org.telegram.ui.Cells;

import java.util.Comparator;
import org.telegram.ui.Cells.ChatMessageCell;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatMessageCell$$ExternalSyntheticLambda7 implements Comparator {
    public static final /* synthetic */ ChatMessageCell$$ExternalSyntheticLambda7 INSTANCE = new ChatMessageCell$$ExternalSyntheticLambda7();

    private /* synthetic */ ChatMessageCell$$ExternalSyntheticLambda7() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$setMessageContent$5;
        lambda$setMessageContent$5 = ChatMessageCell.lambda$setMessageContent$5((ChatMessageCell.PollButton) obj, (ChatMessageCell.PollButton) obj2);
        return lambda$setMessageContent$5;
    }
}
