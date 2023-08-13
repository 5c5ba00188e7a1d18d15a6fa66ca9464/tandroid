package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC$Updates;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda259 implements Comparator {
    public static final /* synthetic */ MessagesController$$ExternalSyntheticLambda259 INSTANCE = new MessagesController$$ExternalSyntheticLambda259();

    private /* synthetic */ MessagesController$$ExternalSyntheticLambda259() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$processChannelsUpdatesQueue$280;
        lambda$processChannelsUpdatesQueue$280 = MessagesController.lambda$processChannelsUpdatesQueue$280((TLRPC$Updates) obj, (TLRPC$Updates) obj2);
        return lambda$processChannelsUpdatesQueue$280;
    }
}
