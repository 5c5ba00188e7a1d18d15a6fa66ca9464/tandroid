package org.telegram.ui;

import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.ui.ChatLinkActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatLinkActivity$ListAdapter$1$$ExternalSyntheticLambda8 implements Runnable {
    public final /* synthetic */ ChatLinkActivity.ListAdapter.AnonymousClass1 f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ TLRPC$Chat f$2;

    public /* synthetic */ ChatLinkActivity$ListAdapter$1$$ExternalSyntheticLambda8(ChatLinkActivity.ListAdapter.AnonymousClass1 anonymousClass1, boolean z, TLRPC$Chat tLRPC$Chat) {
        this.f$0 = anonymousClass1;
        this.f$1 = z;
        this.f$2 = tLRPC$Chat;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onJoinToSendToggle$7(this.f$1, this.f$2);
    }
}
