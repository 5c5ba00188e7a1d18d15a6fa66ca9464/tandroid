package org.telegram.ui;

import java.util.Comparator;
import org.telegram.messenger.MessageObject;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda282 implements Comparator {
    public static final /* synthetic */ ChatActivity$$ExternalSyntheticLambda282 INSTANCE = new ChatActivity$$ExternalSyntheticLambda282();

    private /* synthetic */ ChatActivity$$ExternalSyntheticLambda282() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$updateFilteredMessages$66;
        lambda$updateFilteredMessages$66 = ChatActivity.lambda$updateFilteredMessages$66((MessageObject) obj, (MessageObject) obj2);
        return lambda$updateFilteredMessages$66;
    }
}
