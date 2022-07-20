package org.telegram.ui;

import org.telegram.messenger.MessagesStorage;
import org.telegram.ui.ChatUsersActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatUsersActivity$10$$ExternalSyntheticLambda0 implements MessagesStorage.BooleanCallback {
    public final /* synthetic */ ChatUsersActivity.AnonymousClass10 f$0;

    public /* synthetic */ ChatUsersActivity$10$$ExternalSyntheticLambda0(ChatUsersActivity.AnonymousClass10 anonymousClass10) {
        this.f$0 = anonymousClass10;
    }

    @Override // org.telegram.messenger.MessagesStorage.BooleanCallback
    public final void run(boolean z) {
        this.f$0.lambda$onCovert$0(z);
    }
}
