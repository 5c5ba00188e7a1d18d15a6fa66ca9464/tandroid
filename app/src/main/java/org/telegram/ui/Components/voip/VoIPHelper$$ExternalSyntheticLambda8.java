package org.telegram.ui.Components.voip;

import android.content.DialogInterface;
/* loaded from: classes3.dex */
public final /* synthetic */ class VoIPHelper$$ExternalSyntheticLambda8 implements DialogInterface.OnDismissListener {
    public final /* synthetic */ Runnable f$0;

    public /* synthetic */ VoIPHelper$$ExternalSyntheticLambda8(Runnable runnable) {
        this.f$0 = runnable;
    }

    @Override // android.content.DialogInterface.OnDismissListener
    public final void onDismiss(DialogInterface dialogInterface) {
        VoIPHelper.lambda$showRateAlert$12(this.f$0, dialogInterface);
    }
}
