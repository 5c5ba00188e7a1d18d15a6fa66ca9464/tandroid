package org.telegram.ui;

import java.util.Comparator;
import org.telegram.messenger.MessageObject;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda280 implements Comparator {
    public static final /* synthetic */ ChatActivity$$ExternalSyntheticLambda280 INSTANCE = new ChatActivity$$ExternalSyntheticLambda280();

    private /* synthetic */ ChatActivity$$ExternalSyntheticLambda280() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$checkGroupMessagesOrder$329;
        lambda$checkGroupMessagesOrder$329 = ChatActivity.lambda$checkGroupMessagesOrder$329((MessageObject) obj, (MessageObject) obj2);
        return lambda$checkGroupMessagesOrder$329;
    }
}
