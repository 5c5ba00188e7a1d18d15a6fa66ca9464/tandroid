package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC$Updates;
/* loaded from: classes3.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda294 implements Comparator {
    public static final /* synthetic */ MessagesController$$ExternalSyntheticLambda294 INSTANCE = new MessagesController$$ExternalSyntheticLambda294();

    private /* synthetic */ MessagesController$$ExternalSyntheticLambda294() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$processUpdatesQueue$296;
        lambda$processUpdatesQueue$296 = MessagesController.lambda$processUpdatesQueue$296((TLRPC$Updates) obj, (TLRPC$Updates) obj2);
        return lambda$processUpdatesQueue$296;
    }
}
