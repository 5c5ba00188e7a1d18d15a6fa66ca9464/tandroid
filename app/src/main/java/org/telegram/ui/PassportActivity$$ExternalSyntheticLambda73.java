package org.telegram.ui;

import org.telegram.ui.PassportActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class PassportActivity$$ExternalSyntheticLambda73 implements PassportActivity.ErrorRunnable {
    public final /* synthetic */ PassportActivity f$0;

    public /* synthetic */ PassportActivity$$ExternalSyntheticLambda73(PassportActivity passportActivity) {
        this.f$0 = passportActivity;
    }

    @Override // org.telegram.ui.PassportActivity.ErrorRunnable
    public final void onError(String str, String str2) {
        this.f$0.lambda$addField$62(str, str2);
    }
}
