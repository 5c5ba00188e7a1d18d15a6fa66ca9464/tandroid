package org.telegram.ui;

import java.util.Comparator;
import org.telegram.messenger.MessageObject;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda259 implements Comparator {
    public static final /* synthetic */ ChatActivity$$ExternalSyntheticLambda259 INSTANCE = new ChatActivity$$ExternalSyntheticLambda259();

    private /* synthetic */ ChatActivity$$ExternalSyntheticLambda259() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$checkGroupMessagesOrder$307;
        lambda$checkGroupMessagesOrder$307 = ChatActivity.lambda$checkGroupMessagesOrder$307((MessageObject) obj, (MessageObject) obj2);
        return lambda$checkGroupMessagesOrder$307;
    }
}
