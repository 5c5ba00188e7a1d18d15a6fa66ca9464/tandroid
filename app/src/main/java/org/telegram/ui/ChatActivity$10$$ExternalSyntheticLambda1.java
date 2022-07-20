package org.telegram.ui;

import org.telegram.messenger.MessagesStorage;
import org.telegram.ui.ChatActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$10$$ExternalSyntheticLambda1 implements MessagesStorage.BooleanCallback {
    public final /* synthetic */ ChatActivity.AnonymousClass10 f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ boolean f$2;

    public /* synthetic */ ChatActivity$10$$ExternalSyntheticLambda1(ChatActivity.AnonymousClass10 anonymousClass10, int i, boolean z) {
        this.f$0 = anonymousClass10;
        this.f$1 = i;
        this.f$2 = z;
    }

    @Override // org.telegram.messenger.MessagesStorage.BooleanCallback
    public final void run(boolean z) {
        this.f$0.lambda$onItemClick$1(this.f$1, this.f$2, z);
    }
}
