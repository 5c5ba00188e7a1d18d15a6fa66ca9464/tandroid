package org.telegram.ui;

import android.content.DialogInterface;
import org.telegram.messenger.LocaleController;
/* loaded from: classes3.dex */
public final /* synthetic */ class LanguageSelectActivity$$ExternalSyntheticLambda0 implements DialogInterface.OnClickListener {
    public final /* synthetic */ LanguageSelectActivity f$0;
    public final /* synthetic */ LocaleController.LocaleInfo f$1;

    public /* synthetic */ LanguageSelectActivity$$ExternalSyntheticLambda0(LanguageSelectActivity languageSelectActivity, LocaleController.LocaleInfo localeInfo) {
        this.f$0 = languageSelectActivity;
        this.f$1 = localeInfo;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$createView$2(this.f$1, dialogInterface, i);
    }
}
