package org.telegram.ui;

import android.content.DialogInterface;
/* loaded from: classes3.dex */
public final /* synthetic */ class ThemeSetUrlActivity$$ExternalSyntheticLambda0 implements DialogInterface.OnCancelListener {
    public final /* synthetic */ ThemeSetUrlActivity f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ ThemeSetUrlActivity$$ExternalSyntheticLambda0(ThemeSetUrlActivity themeSetUrlActivity, int i) {
        this.f$0 = themeSetUrlActivity;
        this.f$1 = i;
    }

    @Override // android.content.DialogInterface.OnCancelListener
    public final void onCancel(DialogInterface dialogInterface) {
        this.f$0.lambda$saveTheme$13(this.f$1, dialogInterface);
    }
}
