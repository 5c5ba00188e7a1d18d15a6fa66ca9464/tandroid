package org.telegram.ui;

import org.telegram.messenger.MessagesStorage;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatEditTypeActivity$$ExternalSyntheticLambda14 implements MessagesStorage.LongCallback {
    public final /* synthetic */ ChatEditTypeActivity f$0;

    public /* synthetic */ ChatEditTypeActivity$$ExternalSyntheticLambda14(ChatEditTypeActivity chatEditTypeActivity) {
        this.f$0 = chatEditTypeActivity;
    }

    @Override // org.telegram.messenger.MessagesStorage.LongCallback
    public final void run(long j) {
        this.f$0.lambda$tryUpdateJoinSettings$7(j);
    }
}
