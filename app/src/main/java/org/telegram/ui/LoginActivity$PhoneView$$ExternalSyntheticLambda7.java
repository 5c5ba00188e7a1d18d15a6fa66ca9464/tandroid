package org.telegram.ui;

import android.content.Context;
import android.view.View;
import android.widget.ViewSwitcher;
import org.telegram.ui.LoginActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class LoginActivity$PhoneView$$ExternalSyntheticLambda7 implements ViewSwitcher.ViewFactory {
    public final /* synthetic */ Context f$0;

    public /* synthetic */ LoginActivity$PhoneView$$ExternalSyntheticLambda7(Context context) {
        this.f$0 = context;
    }

    @Override // android.widget.ViewSwitcher.ViewFactory
    public final View makeView() {
        View lambda$new$0;
        lambda$new$0 = LoginActivity.PhoneView.lambda$new$0(this.f$0);
        return lambda$new$0;
    }
}
