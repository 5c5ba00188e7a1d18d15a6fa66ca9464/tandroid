package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes3.dex */
public final /* synthetic */ class LaunchActivity$$ExternalSyntheticLambda85 implements RequestDelegate {
    public final /* synthetic */ LaunchActivity f$0;
    public final /* synthetic */ Theme.ThemeInfo f$1;

    public /* synthetic */ LaunchActivity$$ExternalSyntheticLambda85(LaunchActivity launchActivity, Theme.ThemeInfo themeInfo) {
        this.f$0 = launchActivity;
        this.f$1 = themeInfo;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$didReceivedNotification$91(this.f$1, tLObject, tLRPC$TL_error);
    }
}
