package org.telegram.ui.Components;

import org.telegram.ui.Components.Bulletin;
/* loaded from: classes3.dex */
public final /* synthetic */ class Bulletin$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ Bulletin.Layout f$0;

    public /* synthetic */ Bulletin$$ExternalSyntheticLambda1(Bulletin.Layout layout) {
        this.f$0 = layout;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.onExitTransitionStart();
    }
}
