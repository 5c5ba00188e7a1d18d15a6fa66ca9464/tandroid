package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC$Updates;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda260 implements Comparator {
    public static final /* synthetic */ MessagesController$$ExternalSyntheticLambda260 INSTANCE = new MessagesController$$ExternalSyntheticLambda260();

    private /* synthetic */ MessagesController$$ExternalSyntheticLambda260() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$processUpdatesQueue$284;
        lambda$processUpdatesQueue$284 = MessagesController.lambda$processUpdatesQueue$284((TLRPC$Updates) obj, (TLRPC$Updates) obj2);
        return lambda$processUpdatesQueue$284;
    }
}
