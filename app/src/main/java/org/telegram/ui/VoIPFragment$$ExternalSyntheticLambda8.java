package org.telegram.ui;

import android.content.DialogInterface;
/* loaded from: classes3.dex */
public final /* synthetic */ class VoIPFragment$$ExternalSyntheticLambda8 implements DialogInterface.OnDismissListener {
    public final /* synthetic */ VoIPFragment f$0;

    public /* synthetic */ VoIPFragment$$ExternalSyntheticLambda8(VoIPFragment voIPFragment) {
        this.f$0 = voIPFragment;
    }

    @Override // android.content.DialogInterface.OnDismissListener
    public final void onDismiss(DialogInterface dialogInterface) {
        this.f$0.lambda$showErrorDialog$29(dialogInterface);
    }
}
