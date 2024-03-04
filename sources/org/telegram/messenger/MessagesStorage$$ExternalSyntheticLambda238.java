package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC$Message;
/* loaded from: classes3.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda238 implements Comparator {
    public static final /* synthetic */ MessagesStorage$$ExternalSyntheticLambda238 INSTANCE = new MessagesStorage$$ExternalSyntheticLambda238();

    private /* synthetic */ MessagesStorage$$ExternalSyntheticLambda238() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$getMessagesInternal$151;
        lambda$getMessagesInternal$151 = MessagesStorage.lambda$getMessagesInternal$151((TLRPC$Message) obj, (TLRPC$Message) obj2);
        return lambda$getMessagesInternal$151;
    }
}
