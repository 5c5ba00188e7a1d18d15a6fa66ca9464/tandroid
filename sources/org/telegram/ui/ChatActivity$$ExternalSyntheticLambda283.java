package org.telegram.ui;

import java.util.Comparator;
import org.telegram.messenger.MessageObject;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda283 implements Comparator {
    public static final /* synthetic */ ChatActivity$$ExternalSyntheticLambda283 INSTANCE = new ChatActivity$$ExternalSyntheticLambda283();

    private /* synthetic */ ChatActivity$$ExternalSyntheticLambda283() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$updateFilteredMessages$67;
        lambda$updateFilteredMessages$67 = ChatActivity.lambda$updateFilteredMessages$67((MessageObject) obj, (MessageObject) obj2);
        return lambda$updateFilteredMessages$67;
    }
}
