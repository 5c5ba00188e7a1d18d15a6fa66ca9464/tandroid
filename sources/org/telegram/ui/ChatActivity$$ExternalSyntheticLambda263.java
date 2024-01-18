package org.telegram.ui;

import java.util.Comparator;
import org.telegram.messenger.MessageObject;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda263 implements Comparator {
    public static final /* synthetic */ ChatActivity$$ExternalSyntheticLambda263 INSTANCE = new ChatActivity$$ExternalSyntheticLambda263();

    private /* synthetic */ ChatActivity$$ExternalSyntheticLambda263() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$checkGroupMessagesOrder$311;
        lambda$checkGroupMessagesOrder$311 = ChatActivity.lambda$checkGroupMessagesOrder$311((MessageObject) obj, (MessageObject) obj2);
        return lambda$checkGroupMessagesOrder$311;
    }
}
