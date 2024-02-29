package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC$Updates;
/* loaded from: classes3.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda293 implements Comparator {
    public static final /* synthetic */ MessagesController$$ExternalSyntheticLambda293 INSTANCE = new MessagesController$$ExternalSyntheticLambda293();

    private /* synthetic */ MessagesController$$ExternalSyntheticLambda293() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$processUpdatesQueue$297;
        lambda$processUpdatesQueue$297 = MessagesController.lambda$processUpdatesQueue$297((TLRPC$Updates) obj, (TLRPC$Updates) obj2);
        return lambda$processUpdatesQueue$297;
    }
}
