package org.telegram.messenger;

import java.util.ArrayList;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaController$$ExternalSyntheticLambda27 implements Runnable {
    public final /* synthetic */ MediaController f$0;
    public final /* synthetic */ ArrayList f$1;

    public /* synthetic */ MediaController$$ExternalSyntheticLambda27(MediaController mediaController, ArrayList arrayList) {
        this.f$0 = mediaController;
        this.f$1 = arrayList;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$processMediaObserver$6(this.f$1);
    }
}
