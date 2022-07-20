package org.telegram.ui;

import org.telegram.ui.Components.PasscodeView;
/* loaded from: classes3.dex */
public final /* synthetic */ class BubbleActivity$$ExternalSyntheticLambda0 implements PasscodeView.PasscodeViewDelegate {
    public final /* synthetic */ BubbleActivity f$0;

    public /* synthetic */ BubbleActivity$$ExternalSyntheticLambda0(BubbleActivity bubbleActivity) {
        this.f$0 = bubbleActivity;
    }

    @Override // org.telegram.ui.Components.PasscodeView.PasscodeViewDelegate
    public final void didAcceptedPassword() {
        this.f$0.lambda$showPasscodeActivity$0();
    }
}
