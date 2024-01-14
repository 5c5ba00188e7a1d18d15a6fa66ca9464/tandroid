package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC$Updates;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda272 implements Comparator {
    public static final /* synthetic */ MessagesController$$ExternalSyntheticLambda272 INSTANCE = new MessagesController$$ExternalSyntheticLambda272();

    private /* synthetic */ MessagesController$$ExternalSyntheticLambda272() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$processChannelsUpdatesQueue$287;
        lambda$processChannelsUpdatesQueue$287 = MessagesController.lambda$processChannelsUpdatesQueue$287((TLRPC$Updates) obj, (TLRPC$Updates) obj2);
        return lambda$processChannelsUpdatesQueue$287;
    }
}
