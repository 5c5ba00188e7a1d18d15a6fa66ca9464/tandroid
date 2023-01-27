package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC$Message;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda214 implements Comparator {
    public static final /* synthetic */ MessagesStorage$$ExternalSyntheticLambda214 INSTANCE = new MessagesStorage$$ExternalSyntheticLambda214();

    private /* synthetic */ MessagesStorage$$ExternalSyntheticLambda214() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$getMessagesInternal$134;
        lambda$getMessagesInternal$134 = MessagesStorage.lambda$getMessagesInternal$134((TLRPC$Message) obj, (TLRPC$Message) obj2);
        return lambda$getMessagesInternal$134;
    }
}
