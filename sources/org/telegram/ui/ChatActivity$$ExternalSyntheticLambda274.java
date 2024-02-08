package org.telegram.ui;

import java.util.Comparator;
import org.telegram.messenger.MessageObject;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda274 implements Comparator {
    public static final /* synthetic */ ChatActivity$$ExternalSyntheticLambda274 INSTANCE = new ChatActivity$$ExternalSyntheticLambda274();

    private /* synthetic */ ChatActivity$$ExternalSyntheticLambda274() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$checkGroupMessagesOrder$324;
        lambda$checkGroupMessagesOrder$324 = ChatActivity.lambda$checkGroupMessagesOrder$324((MessageObject) obj, (MessageObject) obj2);
        return lambda$checkGroupMessagesOrder$324;
    }
}
