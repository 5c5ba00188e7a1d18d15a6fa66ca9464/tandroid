package org.telegram.ui;

import android.content.DialogInterface;
import android.content.SharedPreferences;
/* loaded from: classes3.dex */
public final /* synthetic */ class PrivacyControlActivity$$ExternalSyntheticLambda2 implements DialogInterface.OnClickListener {
    public final /* synthetic */ PrivacyControlActivity f$0;
    public final /* synthetic */ SharedPreferences f$1;

    public /* synthetic */ PrivacyControlActivity$$ExternalSyntheticLambda2(PrivacyControlActivity privacyControlActivity, SharedPreferences sharedPreferences) {
        this.f$0 = privacyControlActivity;
        this.f$1 = sharedPreferences;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$processDone$7(this.f$1, dialogInterface, i);
    }
}
