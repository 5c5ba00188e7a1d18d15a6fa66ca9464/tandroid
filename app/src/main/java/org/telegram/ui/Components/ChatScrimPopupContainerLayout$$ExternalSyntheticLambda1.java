package org.telegram.ui.Components;

import org.telegram.ui.Components.PopupSwipeBackLayout;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatScrimPopupContainerLayout$$ExternalSyntheticLambda1 implements PopupSwipeBackLayout.OnSwipeBackProgressListener {
    public final /* synthetic */ ChatScrimPopupContainerLayout f$0;

    public /* synthetic */ ChatScrimPopupContainerLayout$$ExternalSyntheticLambda1(ChatScrimPopupContainerLayout chatScrimPopupContainerLayout) {
        this.f$0 = chatScrimPopupContainerLayout;
    }

    @Override // org.telegram.ui.Components.PopupSwipeBackLayout.OnSwipeBackProgressListener
    public final void onSwipeBackProgress(PopupSwipeBackLayout popupSwipeBackLayout, float f, float f2) {
        this.f$0.lambda$setPopupWindowLayout$1(popupSwipeBackLayout, f, f2);
    }
}
