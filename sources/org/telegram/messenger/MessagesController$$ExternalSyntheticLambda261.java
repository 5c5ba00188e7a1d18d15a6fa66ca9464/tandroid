package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC$Updates;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda261 implements Comparator {
    public static final /* synthetic */ MessagesController$$ExternalSyntheticLambda261 INSTANCE = new MessagesController$$ExternalSyntheticLambda261();

    private /* synthetic */ MessagesController$$ExternalSyntheticLambda261() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$processChannelsUpdatesQueue$281;
        lambda$processChannelsUpdatesQueue$281 = MessagesController.lambda$processChannelsUpdatesQueue$281((TLRPC$Updates) obj, (TLRPC$Updates) obj2);
        return lambda$processChannelsUpdatesQueue$281;
    }
}
