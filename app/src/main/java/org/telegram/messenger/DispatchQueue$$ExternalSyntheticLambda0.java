package org.telegram.messenger;

import android.os.Handler;
import android.os.Message;
/* loaded from: classes.dex */
public final /* synthetic */ class DispatchQueue$$ExternalSyntheticLambda0 implements Handler.Callback {
    public final /* synthetic */ DispatchQueue f$0;

    public /* synthetic */ DispatchQueue$$ExternalSyntheticLambda0(DispatchQueue dispatchQueue) {
        this.f$0 = dispatchQueue;
    }

    @Override // android.os.Handler.Callback
    public final boolean handleMessage(Message message) {
        boolean lambda$run$0;
        lambda$run$0 = this.f$0.lambda$run$0(message);
        return lambda$run$0;
    }
}
