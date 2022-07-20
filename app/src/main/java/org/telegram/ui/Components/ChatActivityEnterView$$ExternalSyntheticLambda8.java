package org.telegram.ui.Components;

import android.content.DialogInterface;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivityEnterView$$ExternalSyntheticLambda8 implements DialogInterface.OnClickListener {
    public final /* synthetic */ ChatActivityEnterView f$0;
    public final /* synthetic */ Runnable f$1;

    public /* synthetic */ ChatActivityEnterView$$ExternalSyntheticLambda8(ChatActivityEnterView chatActivityEnterView, Runnable runnable) {
        this.f$0 = chatActivityEnterView;
        this.f$1 = runnable;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$openWebViewMenu$32(this.f$1, dialogInterface, i);
    }
}
