package org.telegram.messenger;

import android.os.Handler;
import android.os.Message;
/* loaded from: classes.dex */
public final /* synthetic */ class DispatchQueueMainThreadSync$$ExternalSyntheticLambda0 implements Handler.Callback {
    public final /* synthetic */ DispatchQueueMainThreadSync f$0;

    public /* synthetic */ DispatchQueueMainThreadSync$$ExternalSyntheticLambda0(DispatchQueueMainThreadSync dispatchQueueMainThreadSync) {
        this.f$0 = dispatchQueueMainThreadSync;
    }

    @Override // android.os.Handler.Callback
    public final boolean handleMessage(Message message) {
        boolean lambda$run$1;
        lambda$run$1 = this.f$0.lambda$run$1(message);
        return lambda$run$1;
    }
}
