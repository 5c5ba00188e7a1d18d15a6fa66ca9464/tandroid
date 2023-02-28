package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC$Message;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda215 implements Comparator {
    public static final /* synthetic */ MessagesStorage$$ExternalSyntheticLambda215 INSTANCE = new MessagesStorage$$ExternalSyntheticLambda215();

    private /* synthetic */ MessagesStorage$$ExternalSyntheticLambda215() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$getMessagesInternal$135;
        lambda$getMessagesInternal$135 = MessagesStorage.lambda$getMessagesInternal$135((TLRPC$Message) obj, (TLRPC$Message) obj2);
        return lambda$getMessagesInternal$135;
    }
}
