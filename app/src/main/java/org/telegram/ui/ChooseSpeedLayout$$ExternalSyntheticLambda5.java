package org.telegram.ui;

import android.view.View;
import org.telegram.ui.Components.PopupSwipeBackLayout;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChooseSpeedLayout$$ExternalSyntheticLambda5 implements View.OnClickListener {
    public final /* synthetic */ PopupSwipeBackLayout f$0;

    public /* synthetic */ ChooseSpeedLayout$$ExternalSyntheticLambda5(PopupSwipeBackLayout popupSwipeBackLayout) {
        this.f$0 = popupSwipeBackLayout;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        this.f$0.closeForeground();
    }
}
