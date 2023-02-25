package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC$Updates;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda237 implements Comparator {
    public static final /* synthetic */ MessagesController$$ExternalSyntheticLambda237 INSTANCE = new MessagesController$$ExternalSyntheticLambda237();

    private /* synthetic */ MessagesController$$ExternalSyntheticLambda237() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$processChannelsUpdatesQueue$269;
        lambda$processChannelsUpdatesQueue$269 = MessagesController.lambda$processChannelsUpdatesQueue$269((TLRPC$Updates) obj, (TLRPC$Updates) obj2);
        return lambda$processChannelsUpdatesQueue$269;
    }
}
