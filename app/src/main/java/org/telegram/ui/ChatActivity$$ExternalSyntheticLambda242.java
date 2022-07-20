package org.telegram.ui;

import org.telegram.tgnet.TLRPC$Document;
import org.telegram.ui.Components.ChatGreetingsView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda242 implements ChatGreetingsView.Listener {
    public final /* synthetic */ ChatActivity f$0;

    public /* synthetic */ ChatActivity$$ExternalSyntheticLambda242(ChatActivity chatActivity) {
        this.f$0 = chatActivity;
    }

    @Override // org.telegram.ui.Components.ChatGreetingsView.Listener
    public final void onGreetings(TLRPC$Document tLRPC$Document) {
        this.f$0.lambda$createView$25(tLRPC$Document);
    }
}
