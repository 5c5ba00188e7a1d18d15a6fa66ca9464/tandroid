package org.telegram.ui.Components;

import android.content.DialogInterface;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatAttachAlert$$ExternalSyntheticLambda2 implements DialogInterface.OnCancelListener {
    public final /* synthetic */ ChatAttachAlert f$0;

    public /* synthetic */ ChatAttachAlert$$ExternalSyntheticLambda2(ChatAttachAlert chatAttachAlert) {
        this.f$0 = chatAttachAlert;
    }

    @Override // android.content.DialogInterface.OnCancelListener
    public final void onCancel(DialogInterface dialogInterface) {
        this.f$0.lambda$dismiss$33(dialogInterface);
    }
}
