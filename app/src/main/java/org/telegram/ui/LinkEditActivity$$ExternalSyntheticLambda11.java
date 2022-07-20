package org.telegram.ui;

import org.telegram.ui.Components.SlideChooseView;
/* loaded from: classes3.dex */
public final /* synthetic */ class LinkEditActivity$$ExternalSyntheticLambda11 implements SlideChooseView.Callback {
    public final /* synthetic */ LinkEditActivity f$0;

    public /* synthetic */ LinkEditActivity$$ExternalSyntheticLambda11(LinkEditActivity linkEditActivity) {
        this.f$0 = linkEditActivity;
    }

    @Override // org.telegram.ui.Components.SlideChooseView.Callback
    public final void onOptionSelected(int i) {
        this.f$0.lambda$createView$3(i);
    }

    @Override // org.telegram.ui.Components.SlideChooseView.Callback
    public /* synthetic */ void onTouchEnd() {
        SlideChooseView.Callback.CC.$default$onTouchEnd(this);
    }
}
