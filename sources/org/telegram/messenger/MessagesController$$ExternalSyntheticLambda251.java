package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC$Updates;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda251 implements Comparator {
    public static final /* synthetic */ MessagesController$$ExternalSyntheticLambda251 INSTANCE = new MessagesController$$ExternalSyntheticLambda251();

    private /* synthetic */ MessagesController$$ExternalSyntheticLambda251() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$processUpdatesQueue$282;
        lambda$processUpdatesQueue$282 = MessagesController.lambda$processUpdatesQueue$282((TLRPC$Updates) obj, (TLRPC$Updates) obj2);
        return lambda$processUpdatesQueue$282;
    }
}
