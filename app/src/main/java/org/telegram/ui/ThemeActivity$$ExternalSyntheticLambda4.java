package org.telegram.ui;

import android.content.DialogInterface;
/* loaded from: classes3.dex */
public final /* synthetic */ class ThemeActivity$$ExternalSyntheticLambda4 implements DialogInterface.OnClickListener {
    public final /* synthetic */ ThemeActivity f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ ThemeActivity$$ExternalSyntheticLambda4(ThemeActivity themeActivity, int i) {
        this.f$0 = themeActivity;
        this.f$1 = i;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$createView$3(this.f$1, dialogInterface, i);
    }
}
