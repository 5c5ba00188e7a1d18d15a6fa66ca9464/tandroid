package org.telegram.ui;

import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.ReactedUsersListView;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChatActivity$101$$ExternalSyntheticLambda1 implements ReactedUsersListView.OnProfileSelectedListener {
    public final /* synthetic */ ChatActivity.AnonymousClass101 f$0;

    public /* synthetic */ ChatActivity$101$$ExternalSyntheticLambda1(ChatActivity.AnonymousClass101 anonymousClass101) {
        this.f$0 = anonymousClass101;
    }

    @Override // org.telegram.ui.Components.ReactedUsersListView.OnProfileSelectedListener
    public final void onProfileSelected(ReactedUsersListView reactedUsersListView, long j) {
        this.f$0.lambda$instantiateItem$0(reactedUsersListView, j);
    }
}
