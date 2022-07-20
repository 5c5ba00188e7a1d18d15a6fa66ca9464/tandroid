package org.telegram.ui;

import org.telegram.ui.CountrySelectActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class PassportActivity$$ExternalSyntheticLambda70 implements CountrySelectActivity.CountrySelectActivityDelegate {
    public final /* synthetic */ PassportActivity f$0;

    public /* synthetic */ PassportActivity$$ExternalSyntheticLambda70(PassportActivity passportActivity) {
        this.f$0 = passportActivity;
    }

    @Override // org.telegram.ui.CountrySelectActivity.CountrySelectActivityDelegate
    public final void didSelectCountry(CountrySelectActivity.Country country) {
        this.f$0.lambda$createAddressInterface$34(country);
    }
}
