package org.telegram.ui;

import org.telegram.messenger.MessagesStorage;
import org.telegram.ui.ChatLinkActivity;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatLinkActivity$ListAdapter$1$$ExternalSyntheticLambda9 implements MessagesStorage.LongCallback {
    public final /* synthetic */ ChatLinkActivity.ListAdapter.AnonymousClass1 f$0;
    public final /* synthetic */ Runnable f$1;

    public /* synthetic */ ChatLinkActivity$ListAdapter$1$$ExternalSyntheticLambda9(ChatLinkActivity.ListAdapter.AnonymousClass1 anonymousClass1, Runnable runnable) {
        this.f$0 = anonymousClass1;
        this.f$1 = runnable;
    }

    @Override // org.telegram.messenger.MessagesStorage.LongCallback
    public final void run(long j) {
        this.f$0.lambda$migrateIfNeeded$0(this.f$1, j);
    }
}
