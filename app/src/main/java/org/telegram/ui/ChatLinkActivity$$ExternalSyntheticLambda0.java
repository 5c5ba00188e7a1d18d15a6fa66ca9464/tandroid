package org.telegram.ui;

import android.content.DialogInterface;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatLinkActivity$$ExternalSyntheticLambda0 implements DialogInterface.OnCancelListener {
    public final /* synthetic */ ChatLinkActivity f$0;

    public /* synthetic */ ChatLinkActivity$$ExternalSyntheticLambda0(ChatLinkActivity chatLinkActivity) {
        this.f$0 = chatLinkActivity;
    }

    @Override // android.content.DialogInterface.OnCancelListener
    public final void onCancel(DialogInterface dialogInterface) {
        this.f$0.lambda$showLinkAlert$7(dialogInterface);
    }
}
