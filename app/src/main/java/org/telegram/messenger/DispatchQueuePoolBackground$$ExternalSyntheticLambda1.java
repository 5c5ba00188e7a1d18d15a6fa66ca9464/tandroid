package org.telegram.messenger;

import java.util.ArrayList;
/* loaded from: classes.dex */
public final /* synthetic */ class DispatchQueuePoolBackground$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ ArrayList f$0;

    public /* synthetic */ DispatchQueuePoolBackground$$ExternalSyntheticLambda1(ArrayList arrayList) {
        this.f$0 = arrayList;
    }

    @Override // java.lang.Runnable
    public final void run() {
        DispatchQueuePoolBackground.lambda$finishCollectUpdateRunnables$2(this.f$0);
    }
}
