package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC$Message;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda222 implements Comparator {
    public static final /* synthetic */ MessagesStorage$$ExternalSyntheticLambda222 INSTANCE = new MessagesStorage$$ExternalSyntheticLambda222();

    private /* synthetic */ MessagesStorage$$ExternalSyntheticLambda222() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$getMessagesInternal$142;
        lambda$getMessagesInternal$142 = MessagesStorage.lambda$getMessagesInternal$142((TLRPC$Message) obj, (TLRPC$Message) obj2);
        return lambda$getMessagesInternal$142;
    }
}
