package org.telegram.ui;

import java.util.ArrayList;
import org.telegram.ui.CountrySelectActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class CountrySelectActivity$CountrySearchAdapter$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ CountrySelectActivity.CountrySearchAdapter f$0;
    public final /* synthetic */ ArrayList f$1;

    public /* synthetic */ CountrySelectActivity$CountrySearchAdapter$$ExternalSyntheticLambda1(CountrySelectActivity.CountrySearchAdapter countrySearchAdapter, ArrayList arrayList) {
        this.f$0 = countrySearchAdapter;
        this.f$1 = arrayList;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$updateSearchResults$1(this.f$1);
    }
}
