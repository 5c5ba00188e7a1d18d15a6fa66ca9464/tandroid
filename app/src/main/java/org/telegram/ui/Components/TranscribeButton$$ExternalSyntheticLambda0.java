package org.telegram.ui.Components;

import org.telegram.messenger.MessageObject;
/* loaded from: classes3.dex */
public final /* synthetic */ class TranscribeButton$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int f$0;
    public final /* synthetic */ MessageObject f$1;

    public /* synthetic */ TranscribeButton$$ExternalSyntheticLambda0(int i, MessageObject messageObject) {
        this.f$0 = i;
        this.f$1 = messageObject;
    }

    @Override // java.lang.Runnable
    public final void run() {
        TranscribeButton.lambda$transcribePressed$5(this.f$0, this.f$1);
    }
}
