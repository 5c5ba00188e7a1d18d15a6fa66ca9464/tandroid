package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC$Updates;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda243 implements Comparator {
    public static final /* synthetic */ MessagesController$$ExternalSyntheticLambda243 INSTANCE = new MessagesController$$ExternalSyntheticLambda243();

    private /* synthetic */ MessagesController$$ExternalSyntheticLambda243() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$processUpdatesQueue$280;
        lambda$processUpdatesQueue$280 = MessagesController.lambda$processUpdatesQueue$280((TLRPC$Updates) obj, (TLRPC$Updates) obj2);
        return lambda$processUpdatesQueue$280;
    }
}
