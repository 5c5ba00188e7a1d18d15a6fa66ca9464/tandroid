package org.telegram.ui.Components;

import org.telegram.messenger.MessageObject;
/* loaded from: classes3.dex */
public final /* synthetic */ class TranscribeButton$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ MessageObject f$0;
    public final /* synthetic */ long f$1;
    public final /* synthetic */ String f$2;

    public /* synthetic */ TranscribeButton$$ExternalSyntheticLambda3(MessageObject messageObject, long j, String str) {
        this.f$0 = messageObject;
        this.f$1 = j;
        this.f$2 = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        TranscribeButton.finishTranscription(this.f$0, this.f$1, this.f$2);
    }
}
