package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC$Updates;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda242 implements Comparator {
    public static final /* synthetic */ MessagesController$$ExternalSyntheticLambda242 INSTANCE = new MessagesController$$ExternalSyntheticLambda242();

    private /* synthetic */ MessagesController$$ExternalSyntheticLambda242() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$processUpdatesQueue$279;
        lambda$processUpdatesQueue$279 = MessagesController.lambda$processUpdatesQueue$279((TLRPC$Updates) obj, (TLRPC$Updates) obj2);
        return lambda$processUpdatesQueue$279;
    }
}
