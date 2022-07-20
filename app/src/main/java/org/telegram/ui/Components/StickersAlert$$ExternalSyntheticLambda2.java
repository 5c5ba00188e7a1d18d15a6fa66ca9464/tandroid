package org.telegram.ui.Components;

import android.content.DialogInterface;
/* loaded from: classes3.dex */
public final /* synthetic */ class StickersAlert$$ExternalSyntheticLambda2 implements DialogInterface.OnShowListener {
    public final /* synthetic */ EditTextBoldCursor f$0;

    public /* synthetic */ StickersAlert$$ExternalSyntheticLambda2(EditTextBoldCursor editTextBoldCursor) {
        this.f$0 = editTextBoldCursor;
    }

    @Override // android.content.DialogInterface.OnShowListener
    public final void onShow(DialogInterface dialogInterface) {
        StickersAlert.lambda$showNameEnterAlert$25(this.f$0, dialogInterface);
    }
}
