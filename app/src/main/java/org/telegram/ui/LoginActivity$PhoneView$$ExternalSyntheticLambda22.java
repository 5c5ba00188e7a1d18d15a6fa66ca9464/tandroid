package org.telegram.ui;

import org.telegram.ui.CountrySelectActivity;
import org.telegram.ui.LoginActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class LoginActivity$PhoneView$$ExternalSyntheticLambda22 implements CountrySelectActivity.CountrySelectActivityDelegate {
    public final /* synthetic */ LoginActivity.PhoneView f$0;

    public /* synthetic */ LoginActivity$PhoneView$$ExternalSyntheticLambda22(LoginActivity.PhoneView phoneView) {
        this.f$0 = phoneView;
    }

    @Override // org.telegram.ui.CountrySelectActivity.CountrySelectActivityDelegate
    public final void didSelectCountry(CountrySelectActivity.Country country) {
        this.f$0.lambda$new$3(country);
    }
}
