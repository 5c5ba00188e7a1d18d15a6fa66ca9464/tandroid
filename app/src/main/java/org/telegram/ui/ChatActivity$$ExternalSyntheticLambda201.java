package org.telegram.ui;

import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.Cells.ChatMessageCell;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda201 implements Runnable {
    public final /* synthetic */ ChatActivity f$0;
    public final /* synthetic */ AlertDialog[] f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ ChatMessageCell f$3;

    public /* synthetic */ ChatActivity$$ExternalSyntheticLambda201(ChatActivity chatActivity, AlertDialog[] alertDialogArr, int i, ChatMessageCell chatMessageCell) {
        this.f$0 = chatActivity;
        this.f$1 = alertDialogArr;
        this.f$2 = i;
        this.f$3 = chatMessageCell;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$didPressMessageUrl$242(this.f$1, this.f$2, this.f$3);
    }
}
