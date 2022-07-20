package org.telegram.ui.Components;

import android.content.DialogInterface;
/* loaded from: classes3.dex */
public final /* synthetic */ class BotWebViewContainer$$ExternalSyntheticLambda1 implements DialogInterface.OnDismissListener {
    public final /* synthetic */ BotWebViewContainer f$0;

    public /* synthetic */ BotWebViewContainer$$ExternalSyntheticLambda1(BotWebViewContainer botWebViewContainer) {
        this.f$0 = botWebViewContainer;
    }

    @Override // android.content.DialogInterface.OnDismissListener
    public final void onDismiss(DialogInterface dialogInterface) {
        this.f$0.lambda$onOpenUri$2(dialogInterface);
    }
}
