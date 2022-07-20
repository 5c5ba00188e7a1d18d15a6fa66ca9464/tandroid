package org.telegram.ui.Components;

import org.telegram.ui.Components.ChatActivityEnterView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivityEnterView$57$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ ChatActivityEnterView.AnonymousClass57 f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ long f$2;

    public /* synthetic */ ChatActivityEnterView$57$$ExternalSyntheticLambda1(ChatActivityEnterView.AnonymousClass57 anonymousClass57, String str, long j) {
        this.f$0 = anonymousClass57;
        this.f$1 = str;
        this.f$2 = j;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onCustomEmojiSelected$0(this.f$1, this.f$2);
    }
}
