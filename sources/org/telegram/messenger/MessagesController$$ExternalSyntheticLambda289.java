package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC$Updates;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda289 implements Comparator {
    public static final /* synthetic */ MessagesController$$ExternalSyntheticLambda289 INSTANCE = new MessagesController$$ExternalSyntheticLambda289();

    private /* synthetic */ MessagesController$$ExternalSyntheticLambda289() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$processChannelsUpdatesQueue$289;
        lambda$processChannelsUpdatesQueue$289 = MessagesController.lambda$processChannelsUpdatesQueue$289((TLRPC$Updates) obj, (TLRPC$Updates) obj2);
        return lambda$processChannelsUpdatesQueue$289;
    }
}
