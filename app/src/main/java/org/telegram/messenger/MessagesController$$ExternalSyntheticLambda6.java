package org.telegram.messenger;

import androidx.core.util.Consumer;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda6 implements Runnable {
    public final /* synthetic */ Consumer f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ MessagesController$$ExternalSyntheticLambda6(Consumer consumer, int i) {
        this.f$0 = consumer;
        this.f$1 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        MessagesController.lambda$getNextReactionMention$1(this.f$0, this.f$1);
    }
}
