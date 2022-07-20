package org.telegram.ui.ActionBar;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes3.dex */
public final /* synthetic */ class Theme$ThemeInfo$$ExternalSyntheticLambda3 implements RequestDelegate {
    public final /* synthetic */ Theme.ThemeInfo f$0;
    public final /* synthetic */ Theme.ThemeInfo f$1;

    public /* synthetic */ Theme$ThemeInfo$$ExternalSyntheticLambda3(Theme.ThemeInfo themeInfo, Theme.ThemeInfo themeInfo2) {
        this.f$0 = themeInfo;
        this.f$1 = themeInfo2;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$didReceivedNotification$2(this.f$1, tLObject, tLRPC$TL_error);
    }
}
