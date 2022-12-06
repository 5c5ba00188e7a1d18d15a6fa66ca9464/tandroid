package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC$Updates;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda235 implements Comparator {
    public static final /* synthetic */ MessagesController$$ExternalSyntheticLambda235 INSTANCE = new MessagesController$$ExternalSyntheticLambda235();

    private /* synthetic */ MessagesController$$ExternalSyntheticLambda235() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$processUpdatesQueue$272;
        lambda$processUpdatesQueue$272 = MessagesController.lambda$processUpdatesQueue$272((TLRPC$Updates) obj, (TLRPC$Updates) obj2);
        return lambda$processUpdatesQueue$272;
    }
}
