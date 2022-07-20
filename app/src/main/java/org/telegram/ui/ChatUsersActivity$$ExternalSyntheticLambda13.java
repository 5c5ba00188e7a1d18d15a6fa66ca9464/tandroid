package org.telegram.ui;

import org.telegram.messenger.MessagesStorage;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatUsersActivity$$ExternalSyntheticLambda13 implements MessagesStorage.LongCallback {
    public final /* synthetic */ ChatUsersActivity f$0;

    public /* synthetic */ ChatUsersActivity$$ExternalSyntheticLambda13(ChatUsersActivity chatUsersActivity) {
        this.f$0 = chatUsersActivity;
    }

    @Override // org.telegram.messenger.MessagesStorage.LongCallback
    public final void run(long j) {
        this.f$0.lambda$processDone$13(j);
    }
}
