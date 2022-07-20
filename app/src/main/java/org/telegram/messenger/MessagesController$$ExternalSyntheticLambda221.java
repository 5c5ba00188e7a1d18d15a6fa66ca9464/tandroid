package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC$Dialog;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda221 implements Comparator {
    public final /* synthetic */ MessagesController f$0;

    public /* synthetic */ MessagesController$$ExternalSyntheticLambda221(MessagesController messagesController) {
        this.f$0 = messagesController;
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$new$7;
        lambda$new$7 = this.f$0.lambda$new$7((TLRPC$Dialog) obj, (TLRPC$Dialog) obj2);
        return lambda$new$7;
    }
}
