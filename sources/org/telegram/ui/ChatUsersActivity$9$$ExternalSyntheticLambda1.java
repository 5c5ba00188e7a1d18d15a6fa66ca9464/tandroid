package org.telegram.ui;

import androidx.core.util.Consumer;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ChatUsersActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatUsersActivity$9$$ExternalSyntheticLambda1 implements Consumer {
    public static final /* synthetic */ ChatUsersActivity$9$$ExternalSyntheticLambda1 INSTANCE = new ChatUsersActivity$9$$ExternalSyntheticLambda1();

    private /* synthetic */ ChatUsersActivity$9$$ExternalSyntheticLambda1() {
    }

    @Override // androidx.core.util.Consumer
    public final void accept(Object obj) {
        ChatUsersActivity.9.lambda$didSelectUsers$1((TLRPC$User) obj);
    }
}
