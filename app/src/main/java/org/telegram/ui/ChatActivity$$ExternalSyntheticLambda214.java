package org.telegram.ui;

import org.telegram.messenger.MessagesStorage;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$$ExternalSyntheticLambda214 implements MessagesStorage.IntCallback {
    public final /* synthetic */ ChatActivity f$0;
    public final /* synthetic */ boolean f$1;

    public /* synthetic */ ChatActivity$$ExternalSyntheticLambda214(ChatActivity chatActivity, boolean z) {
        this.f$0 = chatActivity;
        this.f$1 = z;
    }

    @Override // org.telegram.messenger.MessagesStorage.IntCallback
    public final void run(int i) {
        this.f$0.lambda$processSelectedOption$192(this.f$1, i);
    }
}
