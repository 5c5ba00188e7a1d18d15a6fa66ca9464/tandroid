package org.telegram.messenger;

import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_messages_createChat;
import org.telegram.ui.ActionBar.BaseFragment;
/* loaded from: classes.dex */
public final /* synthetic */ class MessagesController$$ExternalSyntheticLambda169 implements Runnable {
    public final /* synthetic */ MessagesController f$0;
    public final /* synthetic */ TLRPC$TL_error f$1;
    public final /* synthetic */ BaseFragment f$2;
    public final /* synthetic */ TLRPC$TL_messages_createChat f$3;

    public /* synthetic */ MessagesController$$ExternalSyntheticLambda169(MessagesController messagesController, TLRPC$TL_error tLRPC$TL_error, BaseFragment baseFragment, TLRPC$TL_messages_createChat tLRPC$TL_messages_createChat) {
        this.f$0 = messagesController;
        this.f$1 = tLRPC$TL_error;
        this.f$2 = baseFragment;
        this.f$3 = tLRPC$TL_messages_createChat;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$createChat$207(this.f$1, this.f$2, this.f$3);
    }
}
