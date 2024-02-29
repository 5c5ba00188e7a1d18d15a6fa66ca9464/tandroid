package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC$Updates;
/* loaded from: classes3.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda295 implements Comparator {
    public static final /* synthetic */ MessagesController$$ExternalSyntheticLambda295 INSTANCE = new MessagesController$$ExternalSyntheticLambda295();

    private /* synthetic */ MessagesController$$ExternalSyntheticLambda295() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$processChannelsUpdatesQueue$294;
        lambda$processChannelsUpdatesQueue$294 = MessagesController.lambda$processChannelsUpdatesQueue$294((TLRPC$Updates) obj, (TLRPC$Updates) obj2);
        return lambda$processChannelsUpdatesQueue$294;
    }
}
