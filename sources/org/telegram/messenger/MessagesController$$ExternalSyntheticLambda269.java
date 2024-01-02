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
        int lambda$processChannelsUpdatesQueue$286;
        lambda$processChannelsUpdatesQueue$286 = MessagesController.lambda$processChannelsUpdatesQueue$286((TLRPC$Updates) obj, (TLRPC$Updates) obj2);
        return lambda$processChannelsUpdatesQueue$286;
    }
}
