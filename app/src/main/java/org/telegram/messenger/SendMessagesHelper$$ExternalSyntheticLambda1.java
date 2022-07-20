package org.telegram.messenger;

import android.content.DialogInterface;
import org.telegram.ui.ChatActivity;
/* loaded from: classes.dex */
public final /* synthetic */ class SendMessagesHelper$$ExternalSyntheticLambda1 implements DialogInterface.OnClickListener {
    public final /* synthetic */ ChatActivity f$0;

    public /* synthetic */ SendMessagesHelper$$ExternalSyntheticLambda1(ChatActivity chatActivity) {
        this.f$0 = chatActivity;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        SendMessagesHelper.lambda$sendCallback$26(this.f$0, dialogInterface, i);
    }
}
