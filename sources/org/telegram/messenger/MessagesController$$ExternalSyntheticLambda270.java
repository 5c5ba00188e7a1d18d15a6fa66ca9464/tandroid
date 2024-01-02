package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC$Updates;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda270 implements Comparator {
    public static final /* synthetic */ MessagesController$$ExternalSyntheticLambda270 INSTANCE = new MessagesController$$ExternalSyntheticLambda270();

    private /* synthetic */ MessagesController$$ExternalSyntheticLambda270() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$processUpdatesQueue$288;
        lambda$processUpdatesQueue$288 = MessagesController.lambda$processUpdatesQueue$288((TLRPC$Updates) obj, (TLRPC$Updates) obj2);
        return lambda$processUpdatesQueue$288;
    }
}
