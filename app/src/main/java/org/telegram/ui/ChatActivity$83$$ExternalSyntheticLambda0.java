package org.telegram.ui;

import org.telegram.messenger.MessagesStorage;
import org.telegram.ui.ChatActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$83$$ExternalSyntheticLambda0 implements MessagesStorage.BooleanCallback {
    public final /* synthetic */ ChatActivity.AnonymousClass83 f$0;

    public /* synthetic */ ChatActivity$83$$ExternalSyntheticLambda0(ChatActivity.AnonymousClass83 anonymousClass83) {
        this.f$0 = anonymousClass83;
    }

    @Override // org.telegram.messenger.MessagesStorage.BooleanCallback
    public final void run(boolean z) {
        this.f$0.lambda$onCovert$0(z);
    }
}
