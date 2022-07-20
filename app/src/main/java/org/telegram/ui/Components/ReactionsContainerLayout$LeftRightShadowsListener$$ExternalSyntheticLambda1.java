package org.telegram.ui.Components;

import androidx.core.util.Consumer;
import org.telegram.ui.Components.ReactionsContainerLayout;
/* loaded from: classes3.dex */
public final /* synthetic */ class ReactionsContainerLayout$LeftRightShadowsListener$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ ReactionsContainerLayout.LeftRightShadowsListener f$0;

    public /* synthetic */ ReactionsContainerLayout$LeftRightShadowsListener$$ExternalSyntheticLambda1(ReactionsContainerLayout.LeftRightShadowsListener leftRightShadowsListener) {
        this.f$0 = leftRightShadowsListener;
    }

    @Override // androidx.core.util.Consumer
    public final void accept(Object obj) {
        this.f$0.lambda$onScrolled$0((Float) obj);
    }
}
