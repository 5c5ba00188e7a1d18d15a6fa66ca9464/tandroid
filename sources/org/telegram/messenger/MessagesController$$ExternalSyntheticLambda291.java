package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC$Updates;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda291 implements Comparator {
    public static final /* synthetic */ MessagesController$$ExternalSyntheticLambda291 INSTANCE = new MessagesController$$ExternalSyntheticLambda291();

    private /* synthetic */ MessagesController$$ExternalSyntheticLambda291() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$processUpdatesQueue$292;
        lambda$processUpdatesQueue$292 = MessagesController.lambda$processUpdatesQueue$292((TLRPC$Updates) obj, (TLRPC$Updates) obj2);
        return lambda$processUpdatesQueue$292;
    }
}
