package org.telegram.messenger;

import org.telegram.messenger.SendMessagesHelper;
import org.telegram.tgnet.TLObject;
/* loaded from: classes.dex */
public final /* synthetic */ class SendMessagesHelper$ImportingSticker$1$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ SendMessagesHelper.ImportingSticker.AnonymousClass1 f$0;
    public final /* synthetic */ TLObject f$1;
    public final /* synthetic */ Runnable f$2;

    public /* synthetic */ SendMessagesHelper$ImportingSticker$1$$ExternalSyntheticLambda0(SendMessagesHelper.ImportingSticker.AnonymousClass1 anonymousClass1, TLObject tLObject, Runnable runnable) {
        this.f$0 = anonymousClass1;
        this.f$1 = tLObject;
        this.f$2 = runnable;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$run$0(this.f$1, this.f$2);
    }
}
