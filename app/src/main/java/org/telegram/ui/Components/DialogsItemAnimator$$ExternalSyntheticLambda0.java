package org.telegram.ui.Components;

import java.util.ArrayList;
/* loaded from: classes3.dex */
public final /* synthetic */ class DialogsItemAnimator$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ DialogsItemAnimator f$0;
    public final /* synthetic */ ArrayList f$1;

    public /* synthetic */ DialogsItemAnimator$$ExternalSyntheticLambda0(DialogsItemAnimator dialogsItemAnimator, ArrayList arrayList) {
        this.f$0 = dialogsItemAnimator;
        this.f$1 = arrayList;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$runPendingAnimations$1(this.f$1);
    }
}
