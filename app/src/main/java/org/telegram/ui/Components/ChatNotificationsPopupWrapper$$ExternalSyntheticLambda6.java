package org.telegram.ui.Components;

import android.view.View;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatNotificationsPopupWrapper$$ExternalSyntheticLambda6 implements View.OnClickListener {
    public final /* synthetic */ PopupSwipeBackLayout f$0;

    public /* synthetic */ ChatNotificationsPopupWrapper$$ExternalSyntheticLambda6(PopupSwipeBackLayout popupSwipeBackLayout) {
        this.f$0 = popupSwipeBackLayout;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        this.f$0.closeForeground();
    }
}
