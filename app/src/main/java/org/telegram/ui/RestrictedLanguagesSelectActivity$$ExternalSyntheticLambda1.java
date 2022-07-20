package org.telegram.ui;

import java.util.ArrayList;
/* loaded from: classes3.dex */
public final /* synthetic */ class RestrictedLanguagesSelectActivity$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ RestrictedLanguagesSelectActivity f$0;
    public final /* synthetic */ ArrayList f$1;

    public /* synthetic */ RestrictedLanguagesSelectActivity$$ExternalSyntheticLambda1(RestrictedLanguagesSelectActivity restrictedLanguagesSelectActivity, ArrayList arrayList) {
        this.f$0 = restrictedLanguagesSelectActivity;
        this.f$1 = arrayList;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$updateSearchResults$5(this.f$1);
    }
}
