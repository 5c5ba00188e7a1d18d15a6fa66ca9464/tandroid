package org.telegram.ui;

import org.telegram.ui.ChatLinkActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatLinkActivity$ListAdapter$1$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ ChatLinkActivity.ListAdapter.AnonymousClass1 f$0;
    public final /* synthetic */ Runnable f$1;

    public /* synthetic */ ChatLinkActivity$ListAdapter$1$$ExternalSyntheticLambda4(ChatLinkActivity.ListAdapter.AnonymousClass1 anonymousClass1, Runnable runnable) {
        this.f$0 = anonymousClass1;
        this.f$1 = runnable;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onJoinToSendToggle$8(this.f$1);
    }
}
