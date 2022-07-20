package org.telegram.ui;

import org.telegram.ui.LoginActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class LoginActivity$PhoneView$6$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ LoginActivity.PhoneView.AnonymousClass6 f$0;
    public final /* synthetic */ LoginActivity.PhoneNumberConfirmView f$1;
    public final /* synthetic */ String f$2;

    public /* synthetic */ LoginActivity$PhoneView$6$$ExternalSyntheticLambda1(LoginActivity.PhoneView.AnonymousClass6 anonymousClass6, LoginActivity.PhoneNumberConfirmView phoneNumberConfirmView, String str) {
        this.f$0 = anonymousClass6;
        this.f$1 = phoneNumberConfirmView;
        this.f$2 = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onConfirm$1(this.f$1, this.f$2);
    }
}
