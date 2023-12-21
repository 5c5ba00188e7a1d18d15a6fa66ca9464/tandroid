package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC$Updates;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda269 implements Comparator {
    public static final /* synthetic */ MessagesController$$ExternalSyntheticLambda269 INSTANCE = new MessagesController$$ExternalSyntheticLambda269();

    private /* synthetic */ MessagesController$$ExternalSyntheticLambda269() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$processChannelsUpdatesQueue$281;
        lambda$processChannelsUpdatesQueue$281 = MessagesController.lambda$processChannelsUpdatesQueue$281((TLRPC$Updates) obj, (TLRPC$Updates) obj2);
        return lambda$processChannelsUpdatesQueue$281;
    }
}
