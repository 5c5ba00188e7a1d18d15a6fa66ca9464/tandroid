package org.telegram.ui;

import org.telegram.ui.ActionIntroActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class LaunchActivity$$ExternalSyntheticLambda90 implements ActionIntroActivity.ActionIntroQRLoginDelegate {
    public final /* synthetic */ LaunchActivity f$0;
    public final /* synthetic */ ActionIntroActivity f$1;

    public /* synthetic */ LaunchActivity$$ExternalSyntheticLambda90(LaunchActivity launchActivity, ActionIntroActivity actionIntroActivity) {
        this.f$0 = launchActivity;
        this.f$1 = actionIntroActivity;
    }

    @Override // org.telegram.ui.ActionIntroActivity.ActionIntroQRLoginDelegate
    public final void didFindQRCode(String str) {
        this.f$0.lambda$handleIntent$21(this.f$1, str);
    }
}
