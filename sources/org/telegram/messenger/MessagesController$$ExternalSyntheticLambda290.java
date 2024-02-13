package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC$Updates;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda290 implements Comparator {
    public static final /* synthetic */ MessagesController$$ExternalSyntheticLambda290 INSTANCE = new MessagesController$$ExternalSyntheticLambda290();

    private /* synthetic */ MessagesController$$ExternalSyntheticLambda290() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$processUpdatesQueue$291;
        lambda$processUpdatesQueue$291 = MessagesController.lambda$processUpdatesQueue$291((TLRPC$Updates) obj, (TLRPC$Updates) obj2);
        return lambda$processUpdatesQueue$291;
    }
}
