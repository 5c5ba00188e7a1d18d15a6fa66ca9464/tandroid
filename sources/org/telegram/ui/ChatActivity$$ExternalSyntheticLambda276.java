package org.telegram.ui;

import java.util.Comparator;
import org.telegram.messenger.MessageObject;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda276 implements Comparator {
    public static final /* synthetic */ ChatActivity$$ExternalSyntheticLambda276 INSTANCE = new ChatActivity$$ExternalSyntheticLambda276();

    private /* synthetic */ ChatActivity$$ExternalSyntheticLambda276() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$checkGroupMessagesOrder$325;
        lambda$checkGroupMessagesOrder$325 = ChatActivity.lambda$checkGroupMessagesOrder$325((MessageObject) obj, (MessageObject) obj2);
        return lambda$checkGroupMessagesOrder$325;
    }
}
