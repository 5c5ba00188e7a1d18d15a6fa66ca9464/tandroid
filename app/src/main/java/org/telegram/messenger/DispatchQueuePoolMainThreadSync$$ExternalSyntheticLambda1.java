package org.telegram.messenger;
/* loaded from: classes.dex */
public final /* synthetic */ class DispatchQueuePoolMainThreadSync$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ DispatchQueuePoolMainThreadSync f$0;
    public final /* synthetic */ DispatchQueueMainThreadSync f$1;

    public /* synthetic */ DispatchQueuePoolMainThreadSync$$ExternalSyntheticLambda1(DispatchQueuePoolMainThreadSync dispatchQueuePoolMainThreadSync, DispatchQueueMainThreadSync dispatchQueueMainThreadSync) {
        this.f$0 = dispatchQueuePoolMainThreadSync;
        this.f$1 = dispatchQueueMainThreadSync;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$execute$0(this.f$1);
    }
}
