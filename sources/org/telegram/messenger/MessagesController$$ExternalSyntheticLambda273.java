package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC$Updates;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda273 implements Comparator {
    public static final /* synthetic */ MessagesController$$ExternalSyntheticLambda273 INSTANCE = new MessagesController$$ExternalSyntheticLambda273();

    private /* synthetic */ MessagesController$$ExternalSyntheticLambda273() {
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$processUpdatesQueue$290;
        lambda$processUpdatesQueue$290 = MessagesController.lambda$processUpdatesQueue$290((TLRPC$Updates) obj, (TLRPC$Updates) obj2);
        return lambda$processUpdatesQueue$290;
    }
}
