package org.telegram.messenger;

import org.telegram.messenger.MessagesStorage;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesStorage$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ MessagesStorage.IntCallback f$0;
    public final /* synthetic */ int[] f$1;

    public /* synthetic */ MessagesStorage$$ExternalSyntheticLambda4(MessagesStorage.IntCallback intCallback, int[] iArr) {
        this.f$0 = intCallback;
        this.f$1 = iArr;
    }

    @Override // java.lang.Runnable
    public final void run() {
        MessagesStorage.lambda$getDialogMaxMessageId$192(this.f$0, this.f$1);
    }
}
