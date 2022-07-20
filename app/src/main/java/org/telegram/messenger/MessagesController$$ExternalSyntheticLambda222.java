package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC$Update;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda222 implements Comparator {
    public final /* synthetic */ MessagesController f$0;

    public /* synthetic */ MessagesController$$ExternalSyntheticLambda222(MessagesController messagesController) {
        this.f$0 = messagesController;
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$new$8;
        lambda$new$8 = this.f$0.lambda$new$8((TLRPC$Update) obj, (TLRPC$Update) obj2);
        return lambda$new$8;
    }
}
