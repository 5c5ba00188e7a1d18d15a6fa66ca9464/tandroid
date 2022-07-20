package org.telegram.messenger.browser;

import android.content.DialogInterface;
/* loaded from: classes.dex */
public final /* synthetic */ class Browser$$ExternalSyntheticLambda0 implements DialogInterface.OnCancelListener {
    public final /* synthetic */ int f$0;

    public /* synthetic */ Browser$$ExternalSyntheticLambda0(int i) {
        this.f$0 = i;
    }

    @Override // android.content.DialogInterface.OnCancelListener
    public final void onCancel(DialogInterface dialogInterface) {
        Browser.lambda$openUrl$2(this.f$0, dialogInterface);
    }
}
