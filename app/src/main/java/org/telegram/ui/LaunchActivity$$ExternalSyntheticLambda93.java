package org.telegram.ui;

import org.telegram.ui.Components.PasscodeView;
/* loaded from: classes3.dex */
public final /* synthetic */ class LaunchActivity$$ExternalSyntheticLambda93 implements PasscodeView.PasscodeViewDelegate {
    public final /* synthetic */ LaunchActivity f$0;

    public /* synthetic */ LaunchActivity$$ExternalSyntheticLambda93(LaunchActivity launchActivity) {
        this.f$0 = launchActivity;
    }

    @Override // org.telegram.ui.Components.PasscodeView.PasscodeViewDelegate
    public final void didAcceptedPassword() {
        this.f$0.lambda$showPasscodeActivity$9();
    }
}
