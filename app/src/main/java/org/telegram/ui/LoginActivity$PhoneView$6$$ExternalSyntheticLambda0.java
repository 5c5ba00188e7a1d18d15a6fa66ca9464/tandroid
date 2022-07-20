package org.telegram.ui;

import org.telegram.ui.LoginActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class LoginActivity$PhoneView$6$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ LoginActivity.PhoneView.AnonymousClass6 f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ LoginActivity.PhoneNumberConfirmView f$2;

    public /* synthetic */ LoginActivity$PhoneView$6$$ExternalSyntheticLambda0(LoginActivity.PhoneView.AnonymousClass6 anonymousClass6, String str, LoginActivity.PhoneNumberConfirmView phoneNumberConfirmView) {
        this.f$0 = anonymousClass6;
        this.f$1 = str;
        this.f$2 = phoneNumberConfirmView;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onConfirm$0(this.f$1, this.f$2);
    }
}
