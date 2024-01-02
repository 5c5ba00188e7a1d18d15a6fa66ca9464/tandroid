package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC$Message;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda229 implements Comparator {
    public static final /* synthetic */ MessagesStorage$$ExternalSyntheticLambda229 INSTANCE = new MessagesStorage$$ExternalSyntheticLambda229();

    private /* synthetic */ MessagesStorage$$ExternalSyntheticLambda229() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$getMessagesInternal$144;
        lambda$getMessagesInternal$144 = MessagesStorage.lambda$getMessagesInternal$144((TLRPC$Message) obj, (TLRPC$Message) obj2);
        return lambda$getMessagesInternal$144;
    }
}
