package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC$Message;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda230 implements Comparator {
    public static final /* synthetic */ MessagesStorage$$ExternalSyntheticLambda230 INSTANCE = new MessagesStorage$$ExternalSyntheticLambda230();

    private /* synthetic */ MessagesStorage$$ExternalSyntheticLambda230() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$getMessagesInternal$145;
        lambda$getMessagesInternal$145 = MessagesStorage.lambda$getMessagesInternal$145((TLRPC$Message) obj, (TLRPC$Message) obj2);
        return lambda$getMessagesInternal$145;
    }
}
