package org.telegram.ui;

import org.telegram.ui.ChatActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$54$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ ChatActivity.AnonymousClass54 f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ ChatActivity$54$$ExternalSyntheticLambda0(ChatActivity.AnonymousClass54 anonymousClass54, boolean z, int i) {
        this.f$0 = anonymousClass54;
        this.f$1 = z;
        this.f$2 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onUnpin$1(this.f$1, this.f$2);
    }
}
