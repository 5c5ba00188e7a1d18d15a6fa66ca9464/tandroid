package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC$Updates;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda276 implements Comparator {
    public static final /* synthetic */ MessagesController$$ExternalSyntheticLambda276 INSTANCE = new MessagesController$$ExternalSyntheticLambda276();

    private /* synthetic */ MessagesController$$ExternalSyntheticLambda276() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$processUpdatesQueue$289;
        lambda$processUpdatesQueue$289 = MessagesController.lambda$processUpdatesQueue$289((TLRPC$Updates) obj, (TLRPC$Updates) obj2);
        return lambda$processUpdatesQueue$289;
    }
}
