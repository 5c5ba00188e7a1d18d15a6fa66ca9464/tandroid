package org.telegram.ui;

import org.telegram.messenger.MessagesStorage;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatEditActivity$$ExternalSyntheticLambda27 implements MessagesStorage.BooleanCallback {
    public final /* synthetic */ ChatEditActivity f$0;

    public /* synthetic */ ChatEditActivity$$ExternalSyntheticLambda27(ChatEditActivity chatEditActivity) {
        this.f$0 = chatEditActivity;
    }

    @Override // org.telegram.messenger.MessagesStorage.BooleanCallback
    public final void run(boolean z) {
        this.f$0.lambda$createView$24(z);
    }
}
