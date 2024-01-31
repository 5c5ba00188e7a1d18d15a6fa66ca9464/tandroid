package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC$Message;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda236 implements Comparator {
    public static final /* synthetic */ MessagesStorage$$ExternalSyntheticLambda236 INSTANCE = new MessagesStorage$$ExternalSyntheticLambda236();

    private /* synthetic */ MessagesStorage$$ExternalSyntheticLambda236() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$getMessagesInternal$149;
        lambda$getMessagesInternal$149 = MessagesStorage.lambda$getMessagesInternal$149((TLRPC$Message) obj, (TLRPC$Message) obj2);
        return lambda$getMessagesInternal$149;
    }
}
