package org.telegram.ui;

import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ChatUsersActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatUsersActivity$8$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ ChatUsersActivity.AnonymousClass8 f$0;
    public final /* synthetic */ TLRPC$User f$1;

    public /* synthetic */ ChatUsersActivity$8$$ExternalSyntheticLambda0(ChatUsersActivity.AnonymousClass8 anonymousClass8, TLRPC$User tLRPC$User) {
        this.f$0 = anonymousClass8;
        this.f$1 = tLRPC$User;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$didSelectUser$0(this.f$1);
    }
}
