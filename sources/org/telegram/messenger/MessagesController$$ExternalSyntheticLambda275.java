package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC$Updates;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda275 implements Comparator {
    public static final /* synthetic */ MessagesController$$ExternalSyntheticLambda275 INSTANCE = new MessagesController$$ExternalSyntheticLambda275();

    private /* synthetic */ MessagesController$$ExternalSyntheticLambda275() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$processUpdatesQueue$290;
        lambda$processUpdatesQueue$290 = MessagesController.lambda$processUpdatesQueue$290((TLRPC$Updates) obj, (TLRPC$Updates) obj2);
        return lambda$processUpdatesQueue$290;
    }
}
