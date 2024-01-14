package org.telegram.ui;

import java.util.Comparator;
import org.telegram.messenger.MessageObject;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda261 implements Comparator {
    public static final /* synthetic */ ChatActivity$$ExternalSyntheticLambda261 INSTANCE = new ChatActivity$$ExternalSyntheticLambda261();

    private /* synthetic */ ChatActivity$$ExternalSyntheticLambda261() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$checkGroupMessagesOrder$309;
        lambda$checkGroupMessagesOrder$309 = ChatActivity.lambda$checkGroupMessagesOrder$309((MessageObject) obj, (MessageObject) obj2);
        return lambda$checkGroupMessagesOrder$309;
    }
}
