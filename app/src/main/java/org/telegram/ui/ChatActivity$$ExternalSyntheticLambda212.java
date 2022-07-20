package org.telegram.ui;

import org.telegram.messenger.MessagesStorage;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda212 implements MessagesStorage.BooleanCallback {
    public final /* synthetic */ ChatActivity f$0;

    public /* synthetic */ ChatActivity$$ExternalSyntheticLambda212(ChatActivity chatActivity) {
        this.f$0 = chatActivity;
    }

    @Override // org.telegram.messenger.MessagesStorage.BooleanCallback
    public final void run(boolean z) {
        this.f$0.lambda$createView$71(z);
    }
}
