package org.telegram.ui.Components;

import java.util.List;
/* loaded from: classes3.dex */
public final /* synthetic */ class ReactedHeaderView$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ ReactedHeaderView f$0;
    public final /* synthetic */ List f$1;

    public /* synthetic */ ReactedHeaderView$$ExternalSyntheticLambda1(ReactedHeaderView reactedHeaderView, List list) {
        this.f$0 = reactedHeaderView;
        this.f$1 = list;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onAttachedToWindow$0(this.f$1);
    }
}
