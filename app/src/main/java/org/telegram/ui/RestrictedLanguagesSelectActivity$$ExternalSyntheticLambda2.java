package org.telegram.ui;

import java.util.Comparator;
import org.telegram.messenger.LocaleController;
/* loaded from: classes3.dex */
public final /* synthetic */ class RestrictedLanguagesSelectActivity$$ExternalSyntheticLambda2 implements Comparator {
    public final /* synthetic */ LocaleController.LocaleInfo f$0;

    public /* synthetic */ RestrictedLanguagesSelectActivity$$ExternalSyntheticLambda2(LocaleController.LocaleInfo localeInfo) {
        this.f$0 = localeInfo;
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$fillLanguages$4;
        lambda$fillLanguages$4 = RestrictedLanguagesSelectActivity.lambda$fillLanguages$4(this.f$0, (LocaleController.LocaleInfo) obj, (LocaleController.LocaleInfo) obj2);
        return lambda$fillLanguages$4;
    }
}
